# jlatexmath samples

## Imagens

Infelizmente o \includegraphics só funciona quando o System property "java.awt.headless=false".
Isso porque a implementação usa o componente Label do AWT que exige rodar numa máquina
que tenha display. Ou seja, não funcionará numa aplicação no modo server como é o Cogito.

Código:

```latex
\begin{array}{|c|c|c|c|}
  \multicolumn{4}{c}{\shadowbox{\text{\Huge An image from the \LaTeX3 project}}}\cr
  \hline
  \text{Left}\includegraphics[width=1cm,height=1cm]{backend/src/main/resources/assets/logo.png}\text{Right} & 
  \text{Left}\includegraphics[width=1cm,height=1cm,interpolation=bicubic]{backend/src/main/resources/assets/logo.png}\text{Right} & 
  \text{Left}\includegraphics[angle=45,width=1.5cm,height=1.5cm]{backend/src/main/resources/assets/logo.png}\text{Right} & 
  \text{Left}\includegraphics[angle=160,width=1cm,height=1cm]{backend/src/main/resources/assets/logo.png}\text{Right} \cr
  \hline
\end{array}
```

Resultado:

![Imagem](assets\includegraphic_shadowbox.png)

