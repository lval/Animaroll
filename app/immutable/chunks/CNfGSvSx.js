import{R as m,S as y,T as w,z as P,V as R}from"./CvFIKESv.js";function E(e,r){return e===r||(e==null?void 0:e[R])===r}function T(e={},r,o,h){return m(()=>{var c,s;return y(()=>{c=s,s=[],w(()=>{e!==o(...s)&&(r(e,...s),c&&E(o(...c),e)&&r(null,...c))})}),()=>{P(()=>{s&&E(o(...s),e)&&r(null,...s)})}}),e}const g="modulepreload",v=function(e,r){return new URL(e,r).href},S={},q=function(r,o,h){let c=Promise.resolve();if(o&&o.length>0){const a=document.getElementsByTagName("link"),t=document.querySelector("meta[property=csp-nonce]"),d=(t==null?void 0:t.nonce)||(t==null?void 0:t.getAttribute("nonce"));c=Promise.allSettled(o.map(n=>{if(n=v(n,h),n in S)return;S[n]=!0;const l=n.endsWith(".css"),k=l?'[rel="stylesheet"]':"";if(!!h)for(let f=a.length-1;f>=0;f--){const u=a[f];if(u.href===n&&(!l||u.rel==="stylesheet"))return}else if(document.querySelector(`link[href="${n}"]${k}`))return;const i=document.createElement("link");if(i.rel=l?"stylesheet":g,l||(i.as="script"),i.crossOrigin="",i.href=n,d&&i.setAttribute("nonce",d),document.head.appendChild(i),l)return new Promise((f,u)=>{i.addEventListener("load",f),i.addEventListener("error",()=>u(new Error(`Unable to preload CSS for ${n}`)))})}))}function s(a){const t=new Event("vite:preloadError",{cancelable:!0});if(t.payload=a,window.dispatchEvent(t),!t.defaultPrevented)throw a}return c.then(a=>{for(const t of a||[])t.status==="rejected"&&s(t.reason);return r().catch(s)})};export{q as _,T as b};
