import{b as g,h as u,e as p,E as h,H as S,f as k,g as D,i as H,j as I,k as b,l as m,m as v,U as F,n as L}from"./CvFIKESv.js";function U(A,E,[t,s]=[0,0]){u&&t===0&&p();var a=A,f=null,e=null,i=F,N=t>0?h:0,n=!1;const R=(c,l=!0)=>{n=!0,o(l,c)},o=(c,l)=>{if(i===(i=c))return;let T=!1;if(u&&s!==-1){if(t===0){const r=a.data;r===S?s=0:r===k?s=1/0:(s=parseInt(r.substring(1)),s!==s&&(s=i?1/0:-1))}const _=s>t;!!i===_&&(a=D(),H(a),I(!1),T=!0,s=-1)}i?(f?b(f):l&&(f=m(()=>l(a))),e&&v(e,()=>{e=null})):(e?b(e):l&&(e=m(()=>l(a,[t+1,s]))),f&&v(f,()=>{f=null})),T&&I(!0)};g(()=>{n=!1,E(R),n||o(null,null)},N),u&&(a=L)}export{U as i};
