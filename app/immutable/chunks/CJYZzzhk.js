var Xt=t=>{throw TypeError(t)};var je=(t,e,n)=>e.has(t)||Xt("Cannot "+n);var k=(t,e,n)=>(je(t,e,"read from private field"),n?n.call(t):e.get(t)),P=(t,e,n)=>e.has(t)?Xt("Cannot add the same private member more than once"):e instanceof WeakSet?e.add(t):e.set(t,n);import{aG as Ut,aH as $e,at as Zt,ac as C,q as O,ae as N,au as De}from"./CvFIKESv.js";const B=[];function jt(t,e=Ut){let n=null;const r=new Set;function a(o){if($e(t,o)&&(t=o,n)){const c=!B.length;for(const l of r)l[1](),B.push(l,t);if(c){for(let l=0;l<B.length;l+=2)B[l][0](B[l+1]);B.length=0}}}function s(o){a(o(t))}function i(o,c=Ut){const l=[o,c];return r.add(l),r.size===1&&(n=e(a,s)||Ut),o(t),()=>{r.delete(l),r.size===0&&n&&(n(),n=null)}}return{set:a,update:s,subscribe:i}}new URL("sveltekit-internal://");function Fe(t,e){return t==="/"||e==="ignore"?t:e==="never"?t.endsWith("/")?t.slice(0,-1):t:e==="always"&&!t.endsWith("/")?t+"/":t}function Ve(t){return t.split("%25").map(decodeURI).join("%25")}function qe(t){for(const e in t)t[e]=decodeURIComponent(t[e]);return t}function Lt({href:t}){return t.split("#")[0]}function Be(t,e,n,r=!1){const a=new URL(t);Object.defineProperty(a,"searchParams",{value:new Proxy(a.searchParams,{get(i,o){if(o==="get"||o==="getAll"||o==="has")return l=>(n(l),i[o](l));e();const c=Reflect.get(i,o);return typeof c=="function"?c.bind(i):c}}),enumerable:!0,configurable:!0});const s=["href","pathname","search","toString","toJSON"];r&&s.push("hash");for(const i of s)Object.defineProperty(a,i,{get(){return e(),t[i]},enumerable:!0,configurable:!0});return a}function Ge(...t){let e=5381;for(const n of t)if(typeof n=="string"){let r=n.length;for(;r;)e=e*33^n.charCodeAt(--r)}else if(ArrayBuffer.isView(n)){const r=new Uint8Array(n.buffer,n.byteOffset,n.byteLength);let a=r.length;for(;a;)e=e*33^r[--a]}else throw new TypeError("value must be a string or TypedArray");return(e>>>0).toString(36)}function Me(t){const e=atob(t),n=new Uint8Array(e.length);for(let r=0;r<e.length;r++)n[r]=e.charCodeAt(r);return n.buffer}const He=window.fetch;window.fetch=(t,e)=>((t instanceof Request?t.method:(e==null?void 0:e.method)||"GET")!=="GET"&&Y.delete($t(t)),He(t,e));const Y=new Map;function Ke(t,e){const n=$t(t,e),r=document.querySelector(n);if(r!=null&&r.textContent){let{body:a,...s}=JSON.parse(r.textContent);const i=r.getAttribute("data-ttl");return i&&Y.set(n,{body:a,init:s,ttl:1e3*Number(i)}),r.getAttribute("data-b64")!==null&&(a=Me(a)),Promise.resolve(new Response(a,s))}return window.fetch(t,e)}function We(t,e,n){if(Y.size>0){const r=$t(t,n),a=Y.get(r);if(a){if(performance.now()<a.ttl&&["default","force-cache","only-if-cached",void 0].includes(n==null?void 0:n.cache))return new Response(a.body,a.init);Y.delete(r)}}return window.fetch(e,n)}function $t(t,e){let r=`script[data-sveltekit-fetched][data-url=${JSON.stringify(t instanceof Request?t.url:t)}]`;if(e!=null&&e.headers||e!=null&&e.body){const a=[];e.headers&&a.push([...new Headers(e.headers)].join(",")),e.body&&(typeof e.body=="string"||ArrayBuffer.isView(e.body))&&a.push(e.body),r+=`[data-hash="${Ge(...a)}"]`}return r}const Ye=/^(\[)?(\.\.\.)?(\w+)(?:=(\w+))?(\])?$/;function ze(t){const e=[];return{pattern:t==="/"?/^\/$/:new RegExp(`^${Xe(t).map(r=>{const a=/^\[\.\.\.(\w+)(?:=(\w+))?\]$/.exec(r);if(a)return e.push({name:a[1],matcher:a[2],optional:!1,rest:!0,chained:!0}),"(?:/(.*))?";const s=/^\[\[(\w+)(?:=(\w+))?\]\]$/.exec(r);if(s)return e.push({name:s[1],matcher:s[2],optional:!0,rest:!1,chained:!0}),"(?:/([^/]+))?";if(!r)return;const i=r.split(/\[(.+?)\](?!\])/);return"/"+i.map((c,l)=>{if(l%2){if(c.startsWith("x+"))return Tt(String.fromCharCode(parseInt(c.slice(2),16)));if(c.startsWith("u+"))return Tt(String.fromCharCode(...c.slice(2).split("-").map(p=>parseInt(p,16))));const d=Ye.exec(c),[,u,w,f,m]=d;return e.push({name:f,matcher:m,optional:!!u,rest:!!w,chained:w?l===1&&i[0]==="":!1}),w?"(.*?)":u?"([^/]*)?":"([^/]+?)"}return Tt(c)}).join("")}).join("")}/?$`),params:e}}function Je(t){return!/^\([^)]+\)$/.test(t)}function Xe(t){return t.slice(1).split("/").filter(Je)}function Ze(t,e,n){const r={},a=t.slice(1),s=a.filter(o=>o!==void 0);let i=0;for(let o=0;o<e.length;o+=1){const c=e[o];let l=a[o-i];if(c.chained&&c.rest&&i&&(l=a.slice(o-i,o+1).filter(d=>d).join("/"),i=0),l===void 0){c.rest&&(r[c.name]="");continue}if(!c.matcher||n[c.matcher](l)){r[c.name]=l;const d=e[o+1],u=a[o+1];d&&!d.rest&&d.optional&&u&&c.chained&&(i=0),!d&&!u&&Object.keys(r).length===s.length&&(i=0);continue}if(c.optional&&c.chained){i++;continue}return}if(!i)return r}function Tt(t){return t.normalize().replace(/[[\]]/g,"\\$&").replace(/%/g,"%25").replace(/\//g,"%2[Ff]").replace(/\?/g,"%3[Ff]").replace(/#/g,"%23").replace(/[.*+?^${}()|\\]/g,"\\$&")}function Qe({nodes:t,server_loads:e,dictionary:n,matchers:r}){const a=new Set(e);return Object.entries(n).map(([o,[c,l,d]])=>{const{pattern:u,params:w}=ze(o),f={id:o,exec:m=>{const p=u.exec(m);if(p)return Ze(p,w,r)},errors:[1,...d||[]].map(m=>t[m]),layouts:[0,...l||[]].map(i),leaf:s(c)};return f.errors.length=f.layouts.length=Math.max(f.errors.length,f.layouts.length),f});function s(o){const c=o<0;return c&&(o=~o),[c,t[o]]}function i(o){return o===void 0?o:[a.has(o),t[o]]}}function de(t,e=JSON.parse){try{return e(sessionStorage[t])}catch{}}function Qt(t,e,n=JSON.stringify){const r=n(e);try{sessionStorage[t]=r}catch{}}var ce;const x=((ce=globalThis.__sveltekit_1bkknku)==null?void 0:ce.base)??"";var le;const tn=((le=globalThis.__sveltekit_1bkknku)==null?void 0:le.assets)??x,en="1742629335773",pe="sveltekit:snapshot",ge="sveltekit:scroll",me="sveltekit:states",nn="sveltekit:pageurl",M="sveltekit:history",X="sveltekit:navigation",F={tap:1,hover:2,viewport:3,eager:4,off:-1,false:-1},ut=location.origin;function we(t){if(t instanceof URL)return t;let e=document.baseURI;if(!e){const n=document.getElementsByTagName("base");e=n.length?n[0].href:document.URL}return new URL(t,e)}function Dt(){return{x:pageXOffset,y:pageYOffset}}function G(t,e){return t.getAttribute(`data-sveltekit-${e}`)}const te={...F,"":F.hover};function ye(t){let e=t.assignedSlot??t.parentNode;return(e==null?void 0:e.nodeType)===11&&(e=e.host),e}function _e(t,e){for(;t&&t!==e;){if(t.nodeName.toUpperCase()==="A"&&t.hasAttribute("href"))return t;t=ye(t)}}function Ct(t,e,n){let r;try{if(r=new URL(t instanceof SVGAElement?t.href.baseVal:t.href,document.baseURI),n&&r.hash.match(/^#[^/]/)){const o=location.hash.split("#")[1]||"/";r.hash=`#${o}${r.hash}`}}catch{}const a=t instanceof SVGAElement?t.target.baseVal:t.target,s=!r||!!a||kt(r,e,n)||(t.getAttribute("rel")||"").split(/\s+/).includes("external"),i=(r==null?void 0:r.origin)===ut&&t.hasAttribute("download");return{url:r,external:s,target:a,download:i}}function pt(t){let e=null,n=null,r=null,a=null,s=null,i=null,o=t;for(;o&&o!==document.documentElement;)r===null&&(r=G(o,"preload-code")),a===null&&(a=G(o,"preload-data")),e===null&&(e=G(o,"keepfocus")),n===null&&(n=G(o,"noscroll")),s===null&&(s=G(o,"reload")),i===null&&(i=G(o,"replacestate")),o=ye(o);function c(l){switch(l){case"":case"true":return!0;case"off":case"false":return!1;default:return}}return{preload_code:te[r??"off"],preload_data:te[a??"off"],keepfocus:c(e),noscroll:c(n),reload:c(s),replace_state:c(i)}}function ee(t){const e=jt(t);let n=!0;function r(){n=!0,e.update(i=>i)}function a(i){n=!1,e.set(i)}function s(i){let o;return e.subscribe(c=>{(o===void 0||n&&c!==o)&&i(o=c)})}return{notify:r,set:a,subscribe:s}}const ve={v:()=>{}};function rn(){const{set:t,subscribe:e}=jt(!1);let n;async function r(){clearTimeout(n);try{const a=await fetch(`${tn}/app/version.json`,{headers:{pragma:"no-cache","cache-control":"no-cache"}});if(!a.ok)return!1;const i=(await a.json()).version!==en;return i&&(t(!0),ve.v(),clearTimeout(n)),i}catch{return!1}}return{subscribe:e,check:r}}function kt(t,e,n){return t.origin!==ut||!t.pathname.startsWith(e)?!0:n?!(t.pathname===e+"/"||t.pathname===e+"/index.html"||t.protocol==="file:"&&t.pathname.replace(/\/[^/]+\.html?$/,"")===e):!1}function Bn(t){}function ne(t){const e=on(t),n=new ArrayBuffer(e.length),r=new DataView(n);for(let a=0;a<n.byteLength;a++)r.setUint8(a,e.charCodeAt(a));return n}const an="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";function on(t){t.length%4===0&&(t=t.replace(/==?$/,""));let e="",n=0,r=0;for(let a=0;a<t.length;a++)n<<=6,n|=an.indexOf(t[a]),r+=6,r===24&&(e+=String.fromCharCode((n&16711680)>>16),e+=String.fromCharCode((n&65280)>>8),e+=String.fromCharCode(n&255),n=r=0);return r===12?(n>>=4,e+=String.fromCharCode(n)):r===18&&(n>>=2,e+=String.fromCharCode((n&65280)>>8),e+=String.fromCharCode(n&255)),e}const sn=-1,cn=-2,ln=-3,fn=-4,un=-5,hn=-6;function dn(t,e){if(typeof t=="number")return a(t,!0);if(!Array.isArray(t)||t.length===0)throw new Error("Invalid input");const n=t,r=Array(n.length);function a(s,i=!1){if(s===sn)return;if(s===ln)return NaN;if(s===fn)return 1/0;if(s===un)return-1/0;if(s===hn)return-0;if(i)throw new Error("Invalid input");if(s in r)return r[s];const o=n[s];if(!o||typeof o!="object")r[s]=o;else if(Array.isArray(o))if(typeof o[0]=="string"){const c=o[0],l=e==null?void 0:e[c];if(l)return r[s]=l(a(o[1]));switch(c){case"Date":r[s]=new Date(o[1]);break;case"Set":const d=new Set;r[s]=d;for(let f=1;f<o.length;f+=1)d.add(a(o[f]));break;case"Map":const u=new Map;r[s]=u;for(let f=1;f<o.length;f+=2)u.set(a(o[f]),a(o[f+1]));break;case"RegExp":r[s]=new RegExp(o[1],o[2]);break;case"Object":r[s]=Object(o[1]);break;case"BigInt":r[s]=BigInt(o[1]);break;case"null":const w=Object.create(null);r[s]=w;for(let f=1;f<o.length;f+=2)w[o[f]]=a(o[f+1]);break;case"Int8Array":case"Uint8Array":case"Uint8ClampedArray":case"Int16Array":case"Uint16Array":case"Int32Array":case"Uint32Array":case"Float32Array":case"Float64Array":case"BigInt64Array":case"BigUint64Array":{const f=globalThis[c],m=o[1],p=ne(m),h=new f(p);r[s]=h;break}case"ArrayBuffer":{const f=o[1],m=ne(f);r[s]=m;break}default:throw new Error(`Unknown type ${c}`)}}else{const c=new Array(o.length);r[s]=c;for(let l=0;l<o.length;l+=1){const d=o[l];d!==cn&&(c[l]=a(d))}}else{const c={};r[s]=c;for(const l in o){const d=o[l];c[l]=a(d)}}return r[s]}return a(0)}const be=new Set(["load","prerender","csr","ssr","trailingSlash","config"]);[...be];const pn=new Set([...be]);[...pn];function gn(t){return t.filter(e=>e!=null)}class At{constructor(e,n){this.status=e,typeof n=="string"?this.body={message:n}:n?this.body=n:this.body={message:`Error: ${e}`}}toString(){return JSON.stringify(this.body)}}class Ft{constructor(e,n){this.status=e,this.location=n}}class Vt extends Error{constructor(e,n,r){super(r),this.status=e,this.text=n}}const mn="x-sveltekit-invalidated",wn="x-sveltekit-trailing-slash";function gt(t){return t instanceof At||t instanceof Vt?t.status:500}function yn(t){return t instanceof Vt?t.text:"Internal Error"}let U,Z,xt;const _n=Zt.toString().includes("$$")||/function \w+\(\) \{\}/.test(Zt.toString());var et,nt,rt,at,ot,st,it,ct,fe,lt,ue,ft,he;_n?(U={data:{},form:null,error:null,params:{},route:{id:null},state:{},status:-1,url:new URL("https://example.com")},Z={current:null},xt={current:!1}):(U=new(fe=class{constructor(){P(this,et,C({}));P(this,nt,C(null));P(this,rt,C(null));P(this,at,C({}));P(this,ot,C({id:null}));P(this,st,C({}));P(this,it,C(-1));P(this,ct,C(new URL("https://example.com")))}get data(){return O(k(this,et))}set data(e){N(k(this,et),e)}get form(){return O(k(this,nt))}set form(e){N(k(this,nt),e)}get error(){return O(k(this,rt))}set error(e){N(k(this,rt),e)}get params(){return O(k(this,at))}set params(e){N(k(this,at),e)}get route(){return O(k(this,ot))}set route(e){N(k(this,ot),e)}get state(){return O(k(this,st))}set state(e){N(k(this,st),e)}get status(){return O(k(this,it))}set status(e){N(k(this,it),e)}get url(){return O(k(this,ct))}set url(e){N(k(this,ct),e)}},et=new WeakMap,nt=new WeakMap,rt=new WeakMap,at=new WeakMap,ot=new WeakMap,st=new WeakMap,it=new WeakMap,ct=new WeakMap,fe),Z=new(ue=class{constructor(){P(this,lt,C(null))}get current(){return O(k(this,lt))}set current(e){N(k(this,lt),e)}},lt=new WeakMap,ue),xt=new(he=class{constructor(){P(this,ft,C(!1))}get current(){return O(k(this,ft))}set current(e){N(k(this,ft),e)}},ft=new WeakMap,he),ve.v=()=>xt.current=!0);function vn(t){Object.assign(U,t)}const bn="/__data.json",kn=".html__data.json";function An(t){return t.endsWith(".html")?t.replace(/\.html$/,kn):t.replace(/\/$/,"")+bn}const Sn=new Set(["icon","shortcut icon","apple-touch-icon"]),q=de(ge)??{},Q=de(pe)??{},$={url:ee({}),page:ee({}),navigating:jt(null),updated:rn()};function qt(t){q[t]=Dt()}function En(t,e){let n=t+1;for(;q[n];)delete q[n],n+=1;for(n=e+1;Q[n];)delete Q[n],n+=1}function K(t){return location.href=t.href,new Promise(()=>{})}async function ke(){if("serviceWorker"in navigator){const t=await navigator.serviceWorker.getRegistration(x||"/");t&&await t.update()}}function re(){}let Bt,Ot,mt,j,Nt,A;const wt=[],yt=[];let L=null;const ht=new Map,Ae=new Set,Rn=new Set,z=new Set;let v={branch:[],error:null,url:null},Gt=!1,_t=!1,ae=!0,tt=!1,W=!1,Se=!1,Mt=!1,Ee,E,T,V;const J=new Set;async function Kn(t,e,n){var a,s,i,o;document.URL!==location.href&&(location.href=location.href),A=t,await((s=(a=t.hooks).init)==null?void 0:s.call(a)),Bt=Qe(t),j=document.documentElement,Nt=e,Ot=t.nodes[0],mt=t.nodes[1],Ot(),mt(),E=(i=history.state)==null?void 0:i[M],T=(o=history.state)==null?void 0:o[X],E||(E=T=Date.now(),history.replaceState({...history.state,[M]:E,[X]:T},""));const r=q[E];r&&(history.scrollRestoration="manual",scrollTo(r.x,r.y)),n?await $n(Nt,n):await On(A.hash?Fn(new URL(location.href)):location.href,{replaceState:!0}),jn()}function In(){wt.length=0,Mt=!1}function Re(t){yt.some(e=>e==null?void 0:e.snapshot)&&(Q[t]=yt.map(e=>{var n;return(n=e==null?void 0:e.snapshot)==null?void 0:n.capture()}))}function Ie(t){var e;(e=Q[t])==null||e.forEach((n,r)=>{var a,s;(s=(a=yt[r])==null?void 0:a.snapshot)==null||s.restore(n)})}function oe(){qt(E),Qt(ge,q),Re(T),Qt(pe,Q)}async function Ht(t,e,n,r){return dt({type:"goto",url:we(t),keepfocus:e.keepFocus,noscroll:e.noScroll,replace_state:e.replaceState,state:e.state,redirect_count:n,nav_token:r,accept:()=>{e.invalidateAll&&(Mt=!0),e.invalidate&&e.invalidate.forEach(Nn)}})}async function Un(t){if(t.id!==(L==null?void 0:L.id)){const e={};J.add(e),L={id:t.id,token:e,promise:Te({...t,preload:e}).then(n=>(J.delete(e),n.type==="loaded"&&n.state.error&&(L=null),n))}}return L.promise}async function Pt(t){var n;const e=(n=await Et(t,!1))==null?void 0:n.route;e&&await Promise.all([...e.layouts,e.leaf].map(r=>r==null?void 0:r[1]()))}function Ue(t,e,n){var s;v=t.state;const r=document.querySelector("style[data-sveltekit]");r&&r.remove(),Object.assign(U,t.props.page),Ee=new A.root({target:e,props:{...t.props,stores:$,components:yt},hydrate:n,sync:!1}),Ie(T);const a={from:null,to:{params:v.params,route:{id:((s=v.route)==null?void 0:s.id)??null},url:new URL(location.href)},willUnload:!1,type:"enter",complete:Promise.resolve()};z.forEach(i=>i(a)),_t=!0}function vt({url:t,params:e,branch:n,status:r,error:a,route:s,form:i}){let o="never";if(x&&(t.pathname===x||t.pathname===x+"/"))o="always";else for(const f of n)(f==null?void 0:f.slash)!==void 0&&(o=f.slash);t.pathname=Fe(t.pathname,o),t.search=t.search;const c={type:"loaded",state:{url:t,params:e,branch:n,error:a,route:s},props:{constructors:gn(n).map(f=>f.node.component),page:Yt(U)}};i!==void 0&&(c.props.form=i);let l={},d=!U,u=0;for(let f=0;f<Math.max(n.length,v.branch.length);f+=1){const m=n[f],p=v.branch[f];(m==null?void 0:m.data)!==(p==null?void 0:p.data)&&(d=!0),m&&(l={...l,...m.data},d&&(c.props[`data_${u}`]=l),u+=1)}return(!v.url||t.href!==v.url.href||v.error!==a||i!==void 0&&i!==U.form||d)&&(c.props.page={error:a,params:e,route:{id:(s==null?void 0:s.id)??null},state:{},status:r,url:new URL(t),form:i??null,data:d?l:U.data}),c}async function Kt({loader:t,parent:e,url:n,params:r,route:a,server_data_node:s}){var d,u,w;let i=null,o=!0;const c={dependencies:new Set,params:new Set,parent:!1,route:!1,url:!1,search_params:new Set},l=await t();if((d=l.universal)!=null&&d.load){let f=function(...p){for(const h of p){const{href:_}=new URL(h,n);c.dependencies.add(_)}};const m={route:new Proxy(a,{get:(p,h)=>(o&&(c.route=!0),p[h])}),params:new Proxy(r,{get:(p,h)=>(o&&c.params.add(h),p[h])}),data:(s==null?void 0:s.data)??null,url:Be(n,()=>{o&&(c.url=!0)},p=>{o&&c.search_params.add(p)},A.hash),async fetch(p,h){p instanceof Request&&(h={body:p.method==="GET"||p.method==="HEAD"?void 0:await p.blob(),cache:p.cache,credentials:p.credentials,headers:[...p.headers].length?p.headers:void 0,integrity:p.integrity,keepalive:p.keepalive,method:p.method,mode:p.mode,redirect:p.redirect,referrer:p.referrer,referrerPolicy:p.referrerPolicy,signal:p.signal,...h});const{resolved:_,promise:R}=Le(p,h,n);return o&&f(_.href),R},setHeaders:()=>{},depends:f,parent(){return o&&(c.parent=!0),e()},untrack(p){o=!1;try{return p()}finally{o=!0}}};i=await l.universal.load.call(null,m)??null}return{node:l,loader:t,server:s,universal:(u=l.universal)!=null&&u.load?{type:"data",data:i,uses:c}:null,data:i??(s==null?void 0:s.data)??null,slash:((w=l.universal)==null?void 0:w.trailingSlash)??(s==null?void 0:s.slash)}}function Le(t,e,n){let r=t instanceof Request?t.url:t;const a=new URL(r,n);a.origin===n.origin&&(r=a.href.slice(n.origin.length));const s=_t?We(r,a.href,e):Ke(r,e);return{resolved:a,promise:s}}function se(t,e,n,r,a,s){if(Mt)return!0;if(!a)return!1;if(a.parent&&t||a.route&&e||a.url&&n)return!0;for(const i of a.search_params)if(r.has(i))return!0;for(const i of a.params)if(s[i]!==v.params[i])return!0;for(const i of a.dependencies)if(wt.some(o=>o(new URL(i))))return!0;return!1}function Wt(t,e){return(t==null?void 0:t.type)==="data"?t:(t==null?void 0:t.type)==="skip"?e??null:null}function Ln(t,e){if(!t)return new Set(e.searchParams.keys());const n=new Set([...t.searchParams.keys(),...e.searchParams.keys()]);for(const r of n){const a=t.searchParams.getAll(r),s=e.searchParams.getAll(r);a.every(i=>s.includes(i))&&s.every(i=>a.includes(i))&&n.delete(r)}return n}function ie({error:t,url:e,route:n,params:r}){return{type:"loaded",state:{error:t,url:e,route:n,params:r,branch:[]},props:{page:Yt(U),constructors:[]}}}async function Te({id:t,invalidating:e,url:n,params:r,route:a,preload:s}){if((L==null?void 0:L.id)===t)return J.delete(L.token),L.promise;const{errors:i,layouts:o,leaf:c}=a,l=[...o,c];i.forEach(g=>g==null?void 0:g().catch(()=>{})),l.forEach(g=>g==null?void 0:g[1]().catch(()=>{}));let d=null;const u=v.url?t!==bt(v.url):!1,w=v.route?a.id!==v.route.id:!1,f=Ln(v.url,n);let m=!1;const p=l.map((g,y)=>{var D;const b=v.branch[y],S=!!(g!=null&&g[0])&&((b==null?void 0:b.loader)!==g[1]||se(m,w,u,f,(D=b.server)==null?void 0:D.uses,r));return S&&(m=!0),S});if(p.some(Boolean)){try{d=await Ce(n,p)}catch(g){const y=await H(g,{url:n,params:r,route:{id:t}});return J.has(s)?ie({error:y,url:n,params:r,route:a}):St({status:gt(g),error:y,url:n,route:a})}if(d.type==="redirect")return d}const h=d==null?void 0:d.nodes;let _=!1;const R=l.map(async(g,y)=>{var Rt;if(!g)return;const b=v.branch[y],S=h==null?void 0:h[y];if((!S||S.type==="skip")&&g[1]===(b==null?void 0:b.loader)&&!se(_,w,u,f,(Rt=b.universal)==null?void 0:Rt.uses,r))return b;if(_=!0,(S==null?void 0:S.type)==="error")throw S;return Kt({loader:g[1],url:n,params:r,route:a,parent:async()=>{var Jt;const zt={};for(let It=0;It<y;It+=1)Object.assign(zt,(Jt=await R[It])==null?void 0:Jt.data);return zt},server_data_node:Wt(S===void 0&&g[0]?{type:"skip"}:S??null,g[0]?b==null?void 0:b.server:void 0)})});for(const g of R)g.catch(()=>{});const I=[];for(let g=0;g<l.length;g+=1)if(l[g])try{I.push(await R[g])}catch(y){if(y instanceof Ft)return{type:"redirect",location:y.location};if(J.has(s))return ie({error:await H(y,{params:r,url:n,route:{id:a.id}}),url:n,params:r,route:a});let b=gt(y),S;if(h!=null&&h.includes(y))b=y.status??b,S=y.error;else if(y instanceof At)S=y.body;else{if(await $.updated.check())return await ke(),await K(n);S=await H(y,{params:r,url:n,route:{id:a.id}})}const D=await Tn(g,I,i);return D?vt({url:n,params:r,branch:I.slice(0,D.idx).concat(D.node),status:b,error:S,route:a}):await Pe(n,{id:a.id},S,b)}else I.push(void 0);return vt({url:n,params:r,branch:I,status:200,error:null,route:a,form:e?void 0:null})}async function Tn(t,e,n){for(;t--;)if(n[t]){let r=t;for(;!e[r];)r-=1;try{return{idx:r+1,node:{node:await n[t](),loader:n[t],data:{},server:null,universal:null}}}catch{continue}}}async function St({status:t,error:e,url:n,route:r}){const a={};let s=null;if(A.server_loads[0]===0)try{const o=await Ce(n,[!0]);if(o.type!=="data"||o.nodes[0]&&o.nodes[0].type!=="data")throw 0;s=o.nodes[0]??null}catch{(n.origin!==ut||n.pathname!==location.pathname||Gt)&&await K(n)}try{const o=await Kt({loader:Ot,url:n,params:a,route:r,parent:()=>Promise.resolve({}),server_data_node:Wt(s)}),c={node:await mt(),loader:mt,universal:null,server:null,data:null};return vt({url:n,params:a,branch:[o,c],status:t,error:e,route:null})}catch(o){if(o instanceof Ft)return Ht(new URL(o.location,location.href),{},0);throw o}}async function xn(t){const e=t.href;if(ht.has(e))return ht.get(e);let n;try{const r=(async()=>{let a=await A.hooks.reroute({url:new URL(t),fetch:async(s,i)=>Le(s,i,t).promise})??t;if(typeof a=="string"){const s=new URL(t);A.hash?s.hash=a:s.pathname=a,a=s}return a})();ht.set(e,r),n=await r}catch{ht.delete(e);return}return n}async function Et(t,e){if(t&&!kt(t,x,A.hash)){const n=await xn(t);if(!n)return;const r=Pn(n);for(const a of Bt){const s=a.exec(r);if(s)return{id:bt(t),invalidating:e,route:a,params:qe(s),url:t}}}}function Pn(t){return Ve(A.hash?t.hash.replace(/^#/,"").replace(/[?#].+/,""):t.pathname.slice(x.length))||"/"}function bt(t){return(A.hash?t.hash.replace(/^#/,""):t.pathname)+t.search}function xe({url:t,type:e,intent:n,delta:r}){let a=!1;const s=Ne(v,n,t,e);r!==void 0&&(s.navigation.delta=r);const i={...s.navigation,cancel:()=>{a=!0,s.reject(new Error("navigation cancelled"))}};return tt||Ae.forEach(o=>o(i)),a?null:s}async function dt({type:t,url:e,popped:n,keepfocus:r,noscroll:a,replace_state:s,state:i={},redirect_count:o=0,nav_token:c={},accept:l=re,block:d=re}){const u=V;V=c;const w=await Et(e,!1),f=xe({url:e,type:t,delta:n==null?void 0:n.delta,intent:w});if(!f){d(),V===c&&(V=u);return}const m=E,p=T;l(),tt=!0,_t&&$.navigating.set(Z.current=f.navigation);let h=w&&await Te(w);if(!h){if(kt(e,x,A.hash))return await K(e);h=await Pe(e,{id:null},await H(new Vt(404,"Not Found",`Not found: ${e.pathname}`),{url:e,params:{},route:{id:null}}),404)}if(e=(w==null?void 0:w.url)||e,V!==c)return f.reject(new Error("navigation aborted")),!1;if(h.type==="redirect")if(o>=20)h=await St({status:500,error:await H(new Error("Redirect loop"),{url:e,params:{},route:{id:null}}),url:e,route:{id:null}});else return await Ht(new URL(h.location,e).href,{},o+1,c),!1;else h.props.page.status>=400&&await $.updated.check()&&(await ke(),await K(e));if(In(),qt(m),Re(p),h.props.page.url.pathname!==e.pathname&&(e.pathname=h.props.page.url.pathname),i=n?n.state:i,!n){const g=s?0:1,y={[M]:E+=g,[X]:T+=g,[me]:i};(s?history.replaceState:history.pushState).call(history,y,"",e),s||En(E,T)}if(L=null,h.props.page.state=i,_t){v=h.state,h.props.page&&(h.props.page.url=e);const g=(await Promise.all(Array.from(Rn,y=>y(f.navigation)))).filter(y=>typeof y=="function");if(g.length>0){let y=function(){g.forEach(b=>{z.delete(b)})};g.push(y),g.forEach(b=>{z.add(b)})}Ee.$set(h.props),vn(h.props.page),Se=!0}else Ue(h,Nt,!1);const{activeElement:_}=document;await De();const R=n?n.scroll:a?Dt():null;if(ae){const g=e.hash&&document.getElementById(decodeURIComponent(A.hash?e.hash.split("#")[2]??"":e.hash.slice(1)));R?scrollTo(R.x,R.y):g?g.scrollIntoView():scrollTo(0,0)}const I=document.activeElement!==_&&document.activeElement!==document.body;!r&&!I&&Dn(),ae=!0,h.props.page&&Object.assign(U,h.props.page),tt=!1,t==="popstate"&&Ie(T),f.fulfil(void 0),z.forEach(g=>g(f.navigation)),$.navigating.set(Z.current=null)}async function Pe(t,e,n,r){return t.origin===ut&&t.pathname===location.pathname&&!Gt?await St({status:r,error:n,url:t,route:e}):await K(t)}function Cn(){let t,e,n;j.addEventListener("mousemove",o=>{const c=o.target;clearTimeout(t),t=setTimeout(()=>{s(c,F.hover)},20)});function r(o){o.defaultPrevented||s(o.composedPath()[0],F.tap)}j.addEventListener("mousedown",r),j.addEventListener("touchstart",r,{passive:!0});const a=new IntersectionObserver(o=>{for(const c of o)c.isIntersecting&&(Pt(new URL(c.target.href)),a.unobserve(c.target))},{threshold:0});async function s(o,c){const l=_e(o,j),d=l===e&&c>=n;if(!l||d)return;const{url:u,external:w,download:f}=Ct(l,x,A.hash);if(w||f)return;const m=pt(l),p=u&&bt(v.url)===bt(u);if(!(m.reload||p))if(c<=m.preload_data){e=l,n=F.tap;const h=await Et(u,!1);if(!h)return;Un(h)}else c<=m.preload_code&&(e=l,n=c,Pt(u))}function i(){a.disconnect();for(const o of j.querySelectorAll("a")){const{url:c,external:l,download:d}=Ct(o,x,A.hash);if(l||d)continue;const u=pt(o);u.reload||(u.preload_code===F.viewport&&a.observe(o),u.preload_code===F.eager&&Pt(c))}}z.add(i),i()}function H(t,e){if(t instanceof At)return t.body;const n=gt(t),r=yn(t);return A.hooks.handleError({error:t,event:e,status:n,message:r})??{message:r}}function On(t,e={}){return t=new URL(we(t)),t.origin!==ut?Promise.reject(new Error("goto: invalid URL")):Ht(t,e,0)}function Nn(t){if(typeof t=="function")wt.push(t);else{const{href:e}=new URL(t,location.href);wt.push(n=>n.href===e)}}function jn(){var e;history.scrollRestoration="manual",addEventListener("beforeunload",n=>{let r=!1;if(oe(),!tt){const a=Ne(v,void 0,null,"leave"),s={...a.navigation,cancel:()=>{r=!0,a.reject(new Error("navigation cancelled"))}};Ae.forEach(i=>i(s))}r?(n.preventDefault(),n.returnValue=""):history.scrollRestoration="auto"}),addEventListener("visibilitychange",()=>{document.visibilityState==="hidden"&&oe()}),(e=navigator.connection)!=null&&e.saveData||Cn(),j.addEventListener("click",async n=>{if(n.button||n.which!==1||n.metaKey||n.ctrlKey||n.shiftKey||n.altKey||n.defaultPrevented)return;const r=_e(n.composedPath()[0],j);if(!r)return;const{url:a,external:s,target:i,download:o}=Ct(r,x,A.hash);if(!a)return;if(i==="_parent"||i==="_top"){if(window.parent!==window)return}else if(i&&i!=="_self")return;const c=pt(r);if(!(r instanceof SVGAElement)&&a.protocol!==location.protocol&&!(a.protocol==="https:"||a.protocol==="http:")||o)return;const[d,u]=(A.hash?a.hash.replace(/^#/,""):a.href).split("#"),w=d===Lt(location);if(s||c.reload&&(!w||!u)){xe({url:a,type:"link"})?tt=!0:n.preventDefault();return}if(u!==void 0&&w){const[,f]=v.url.href.split("#");if(f===u){if(n.preventDefault(),u===""||u==="top"&&r.ownerDocument.getElementById("top")===null)window.scrollTo({top:0});else{const m=r.ownerDocument.getElementById(decodeURIComponent(u));m&&(m.scrollIntoView(),m.focus())}return}if(W=!0,qt(E),t(a),!c.replace_state)return;W=!1}n.preventDefault(),await new Promise(f=>{requestAnimationFrame(()=>{setTimeout(f,0)}),setTimeout(f,100)}),await dt({type:"link",url:a,keepfocus:c.keepfocus,noscroll:c.noscroll,replace_state:c.replace_state??a.href===location.href})}),j.addEventListener("submit",n=>{if(n.defaultPrevented)return;const r=HTMLFormElement.prototype.cloneNode.call(n.target),a=n.submitter;if(((a==null?void 0:a.formTarget)||r.target)==="_blank"||((a==null?void 0:a.formMethod)||r.method)!=="get")return;const o=new URL((a==null?void 0:a.hasAttribute("formaction"))&&(a==null?void 0:a.formAction)||r.action);if(kt(o,x,!1))return;const c=n.target,l=pt(c);if(l.reload)return;n.preventDefault(),n.stopPropagation();const d=new FormData(c),u=a==null?void 0:a.getAttribute("name");u&&d.append(u,(a==null?void 0:a.getAttribute("value"))??""),o.search=new URLSearchParams(d).toString(),dt({type:"form",url:o,keepfocus:l.keepfocus,noscroll:l.noscroll,replace_state:l.replace_state??o.href===location.href})}),addEventListener("popstate",async n=>{var r;if((r=n.state)!=null&&r[M]){const a=n.state[M];if(V={},a===E)return;const s=q[a],i=n.state[me]??{},o=new URL(n.state[nn]??location.href),c=n.state[X],l=v.url?Lt(location)===Lt(v.url):!1;if(c===T&&(Se||l)){i!==U.state&&(U.state=i),t(o),q[E]=Dt(),s&&scrollTo(s.x,s.y),E=a;return}const u=a-E;await dt({type:"popstate",url:o,popped:{state:i,scroll:s,delta:u},accept:()=>{E=a,T=c},block:()=>{history.go(-u)},nav_token:V})}else if(!W){const a=new URL(location.href);t(a),A.hash&&location.reload()}}),addEventListener("hashchange",()=>{W&&(W=!1,history.replaceState({...history.state,[M]:++E,[X]:T},"",location.href))});for(const n of document.querySelectorAll("link"))Sn.has(n.rel)&&(n.href=n.href);addEventListener("pageshow",n=>{n.persisted&&$.navigating.set(Z.current=null)});function t(n){v.url=U.url=n,$.page.set(Yt(U)),$.page.notify()}}async function $n(t,{status:e=200,error:n,node_ids:r,params:a,route:s,server_route:i,data:o,form:c}){Gt=!0;const l=new URL(location.href);let d;({params:a={},route:s={id:null}}=await Et(l,!1)||{}),d=Bt.find(({id:f})=>f===s.id);let u,w=!0;try{const f=r.map(async(p,h)=>{const _=o[h];return _!=null&&_.uses&&(_.uses=Oe(_.uses)),Kt({loader:A.nodes[p],url:l,params:a,route:s,parent:async()=>{const R={};for(let I=0;I<h;I+=1)Object.assign(R,(await f[I]).data);return R},server_data_node:Wt(_)})}),m=await Promise.all(f);if(d){const p=d.layouts;for(let h=0;h<p.length;h++)p[h]||m.splice(h,0,void 0)}u=vt({url:l,params:a,branch:m,status:e,error:n,form:c,route:d??null})}catch(f){if(f instanceof Ft){await K(new URL(f.location,location.href));return}u=await St({status:gt(f),error:await H(f,{url:l,params:a,route:s}),url:l,route:s}),t.textContent="",w=!1}u.props.page&&(u.props.page.state={}),Ue(u,t,w)}async function Ce(t,e){var s;const n=new URL(t);n.pathname=An(t.pathname),t.pathname.endsWith("/")&&n.searchParams.append(wn,"1"),n.searchParams.append(mn,e.map(i=>i?"1":"0").join(""));const r=window.fetch,a=await r(n.href,{});if(!a.ok){let i;throw(s=a.headers.get("content-type"))!=null&&s.includes("application/json")?i=await a.json():a.status===404?i="Not Found":a.status===500&&(i="Internal Error"),new At(a.status,i)}return new Promise(async i=>{var w;const o=new Map,c=a.body.getReader(),l=new TextDecoder;function d(f){return dn(f,{...A.decoders,Promise:m=>new Promise((p,h)=>{o.set(m,{fulfil:p,reject:h})})})}let u="";for(;;){const{done:f,value:m}=await c.read();if(f&&!u)break;for(u+=!m&&u?`
`:l.decode(m,{stream:!0});;){const p=u.indexOf(`
`);if(p===-1)break;const h=JSON.parse(u.slice(0,p));if(u=u.slice(p+1),h.type==="redirect")return i(h);if(h.type==="data")(w=h.nodes)==null||w.forEach(_=>{(_==null?void 0:_.type)==="data"&&(_.uses=Oe(_.uses),_.data=d(_.data))}),i(h);else if(h.type==="chunk"){const{id:_,data:R,error:I}=h,g=o.get(_);o.delete(_),I?g.reject(d(I)):g.fulfil(d(R))}}}})}function Oe(t){return{dependencies:new Set((t==null?void 0:t.dependencies)??[]),params:new Set((t==null?void 0:t.params)??[]),parent:!!(t!=null&&t.parent),route:!!(t!=null&&t.route),url:!!(t!=null&&t.url),search_params:new Set((t==null?void 0:t.search_params)??[])}}function Dn(){const t=document.querySelector("[autofocus]");if(t)t.focus();else{const e=document.body,n=e.getAttribute("tabindex");e.tabIndex=-1,e.focus({preventScroll:!0,focusVisible:!1}),n!==null?e.setAttribute("tabindex",n):e.removeAttribute("tabindex");const r=getSelection();if(r&&r.type!=="None"){const a=[];for(let s=0;s<r.rangeCount;s+=1)a.push(r.getRangeAt(s));setTimeout(()=>{if(r.rangeCount===a.length){for(let s=0;s<r.rangeCount;s+=1){const i=a[s],o=r.getRangeAt(s);if(i.commonAncestorContainer!==o.commonAncestorContainer||i.startContainer!==o.startContainer||i.endContainer!==o.endContainer||i.startOffset!==o.startOffset||i.endOffset!==o.endOffset)return}r.removeAllRanges()}})}}}function Ne(t,e,n,r){var c,l;let a,s;const i=new Promise((d,u)=>{a=d,s=u});return i.catch(()=>{}),{navigation:{from:{params:t.params,route:{id:((c=t.route)==null?void 0:c.id)??null},url:t.url},to:n&&{params:(e==null?void 0:e.params)??null,route:{id:((l=e==null?void 0:e.route)==null?void 0:l.id)??null},url:n},willUnload:!e,type:r,complete:i},fulfil:a,reject:s}}function Yt(t){return{data:t.data,error:t.error,form:t.form,params:t.params,route:t.route,state:t.state,status:t.status,url:t.url}}function Fn(t){const e=new URL(t);return e.hash=decodeURIComponent(t.hash),e}export{Kn as a,Bn as l,Z as n,U as p,$ as s};
