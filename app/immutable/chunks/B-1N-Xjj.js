import{n as p,q as r,u as g,E as S,H as h,v as D,w as H,x as k,y as I,z as v,A,B as b,U as q,C}from"./oJb2tkNr.js";function L(E,m,[t,s]=[0,0]){r&&t===0&&g();var a=E,f=null,e=null,i=q,N=t>0?S:0,n=!1;const R=(c,l=!0)=>{n=!0,o(l,c)},o=(c,l)=>{if(i===(i=c))return;let T=!1;if(r&&s!==-1){if(t===0){const u=a.data;u===h?s=0:u===D?s=1/0:(s=parseInt(u.substring(1)),s!==s&&(s=i?1/0:-1))}const _=s>t;!!i===_&&(a=H(),k(a),I(!1),T=!0,s=-1)}i?(f?v(f):l&&(f=A(()=>l(a))),e&&b(e,()=>{e=null})):(e?v(e):l&&(e=A(()=>l(a,[t+1,s]))),f&&b(f,()=>{f=null})),T&&I(!0)};p(()=>{n=!1,m(R),n||o(null,null)},N),r&&(a=C)}export{L as i};
