package br.nom.figueiredo.sergio.cogito.service;

import br.nom.figueiredo.sergio.cogito.model.IptableRule;
import br.nom.figueiredo.sergio.cogito.model.Maquina;
import br.nom.figueiredo.sergio.cogito.model.MaquinaIp;
import br.nom.figueiredo.sergio.cogito.repository.MaquinaIpRepository;
import br.nom.figueiredo.sergio.cogito.repository.MaquinaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IptablesServiceImpl implements IptablesService {

    public static final Pattern IPTABLES_RULE_PATTERN = Pattern.compile("^(\\d+)\\s+(\\d+\\w?)\\s+(\\d+\\w?)\\s+(\\w+)\\s+(\\d+)\\s+([^\\s]+)\\s+(\\w+)\\s+(\\w+)\\s+([\\d./]+)\\s+([\\d./]+)\\s*(.*)$");
    private final static Logger LOGGER = LoggerFactory.getLogger(IptablesServiceImpl.class);
    private final MaquinaRepository maquinaRepository;
    private final MaquinaIpRepository maquinaIpRepository;

    public IptablesServiceImpl(MaquinaRepository maquinaRepository, MaquinaIpRepository maquinaIpRepository) {
        this.maquinaRepository = maquinaRepository;
        this.maquinaIpRepository = maquinaIpRepository;
    }

    @Override
    public Flux<IptableRule> getIptablesForwardRules() {

        return Flux.push(emitter -> {

            Thread iptablesThread = new Thread(() -> {
                // executa o comando 'iptables -L' no terminal para capturar os IPs das regras DROP no chain 'FORWARD'
                int exitCode = executaTerminal("sudo iptables -L FORWARD -n --line-numbers -v", linha -> {
                            String result;
                            Matcher matcher = IPTABLES_RULE_PATTERN.matcher(linha);
                            while (matcher.find()) {
                                Integer num = Integer.parseInt(matcher.group(1));
                                String pkts = matcher.group(2);
                                String bytes = matcher.group(3);
                                String target = matcher.group(4);
                                String prot = matcher.group(5);
                                String opt = matcher.group(6);
                                String in = matcher.group(7);
                                String out = matcher.group(8);
                                String source = matcher.group(9);
                                String destination = matcher.group(10);
                                String extra = matcher.group(11);

                                IptableRule iptableRule = new IptableRule();
                                iptableRule.setNum(num);
                                iptableRule.setTarget(target);
                                iptableRule.setIn(in);
                                iptableRule.setOut(out);
                                iptableRule.setSource(source);
                                emitter.next(iptableRule);
                            }
                        }
                );

                if (exitCode == 0) {
                    emitter.complete();
                } else {
                    emitter.error(new CogitoServiceException(String.format("iptables retornou %d", exitCode)));
                }

            }, "iptablesRules");

            iptablesThread.setDaemon(true);
            iptablesThread.start();

        });

    }

    @Override
    public Flux<IptableRule> addDropRule(Maquina maquina) {
        LOGGER.trace("addDropRule({})", maquina.getNome());
        Objects.requireNonNull(maquina);
        Objects.requireNonNull(maquina.getIps());

        List<String> ipsDaMaquina = maquina.getIps().stream()
                .map(MaquinaIp::getIp)
                .toList();

        List<String> ipsParaDrop = new ArrayList<>(ipsDaMaquina);

        return Mono.just(ipsParaDrop)
                /* remove da lista, todos os IPs que já estão com DROP */
                .delayUntil(ipsParaDrop2 -> this.getIptablesForwardRules()
                        /* pega os IPs da máquina que já estão com rule DROP */
                        .filter(rule -> "DROP".equals(rule.getTarget()) &&
                                        "lan0".equals(rule.getIn()) &&
                                        "wlan0".equals(rule.getOut()) &&
                                        ipsParaDrop2.contains(rule.getSource()))
                        .map(IptableRule::getSource)
                        .doOnNext(ipsParaDrop2::remove))
                /* cria regra DROP para os IPs restantes */
                .flatMapMany(Flux::fromIterable)
                .doOnNext(this::addDropRuleByIP)
                /* e lista todas as regras DROP relacionadas aos IPs da máquina */
                .thenMany(this.getIptablesForwardRules()
                        .filter(rule -> "DROP".equals(rule.getTarget()) &&
                                        "lan0".equals(rule.getIn()) &&
                                        "wlan0".equals(rule.getOut()) &&
                                        ipsDaMaquina.contains(rule.getSource())));

    }

    private void addDropRuleByIP(String ip) {
        int exitCode = this.executaTerminal(String.format("sudo iptables -I FORWARD -i lan0 -o wlan0 -s %s -j DROP", ip),
                LOGGER::debug);
        if (exitCode != 0) {
            throw new CogitoServiceException(String.format("Falha ao criar drop rule para IP %s", ip));
        }
    }

    @Override
    public Mono<Void> deleteDropRule(Maquina maquina) {
        LOGGER.trace("deleteDropRule({})", maquina.getNome());
        Objects.requireNonNull(maquina);
        Objects.requireNonNull(maquina.getIps());

        List<String> ipsDaMaquina = maquina.getIps().stream()
                .map(MaquinaIp::getIp)
                .toList();

        return this.getIptablesForwardRules()
                /* pega os IPs da máquina que já estão com rule DROP */
                .filter(rule -> "DROP".equals(rule.getTarget()) &&
                                "lan0".equals(rule.getIn()) &&
                                "wlan0".equals(rule.getOut()) &&
                                ipsDaMaquina.contains(rule.getSource()))
                .map(IptableRule::getSource)
                .distinct()
                /* remove a regra encontrada do iptables */
                .doOnNext(this::deleteDropRuleByIP)
                .then();

    }

    private void deleteDropRuleByIP(String ip) {
        int exitCode = this.executaTerminal(String.format("sudo iptables -D FORWARD -i lan0 -o wlan0 -s %s -j DROP", ip),
                LOGGER::debug);
        if (exitCode != 0) {
            throw new CogitoServiceException(String.format("Falha ao criar drop rule para IP %s", ip));
        }
    }


    private int executaTerminal(String comando, Consumer<String> linhaConsumer) {
        try {
            Runtime rt = Runtime.getRuntime();

            LOGGER.info("Comando: [{}]", comando);

            Process pr = rt.exec(comando);

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    new SequenceInputStream(pr.getInputStream(), pr.getErrorStream())));

            String line;
            while ((line = input.readLine()) != null) {
                LOGGER.debug(line);
                linhaConsumer.accept(line);
            }

            int exitCode = pr.waitFor();
            LOGGER.info("Exited with error code {}", exitCode);
            return exitCode;
        } catch (Exception e) {
            LOGGER.error("Erro ao executar comando no terminal.", e);
            return -1;
        }

    }
}
