import {HttpInterceptorFn} from '@angular/common/http';
import {environment} from "../environments/environment";

export const baseUrlInterceptor: HttpInterceptorFn = (req, next) => {
  const apiReq = req.clone({
    url: `${environment.baseUrl}/${req.url}`,
/*
    setHeaders: {
      'Access-Control-Allow-Origin': '*',
    }
*/
  });
  return next(apiReq);
};
