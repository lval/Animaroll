import{h as o}from"./CvFIKESv.js";const a=[...` 	
\r\f \v\uFEFF`];function c(t,g,u){var r=t==null?"":""+t;if(u){for(var l in u)if(u[l])r=r?r+" "+l:l;else if(r.length)for(var f=l.length,i=0;(i=r.indexOf(l,i))>=0;){var n=i+f;(i===0||a.includes(r[i-1]))&&(n===r.length||a.includes(r[n]))?r=(i===0?"":r.substring(0,i))+r.substring(n+1):i=n}}return r===""?null:r}function N(t,g){return t==null?null:String(t)}function b(t,g,u,r,l,f){var i=t.__className;if(o||i!==u){var n=c(u,r,f);(!o||n!==t.getAttribute("class"))&&(n==null?t.removeAttribute("class"):t.className=n),t.__className=u}else if(f&&l!==f)for(var s in f){var h=!!f[s];(l==null||h!==!!l[s])&&t.classList.toggle(s,h)}return f}export{b as s,N as t};
