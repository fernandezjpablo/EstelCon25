(()=>{"use strict";var e={442:(e,o,a)=>{var n=a(751),t=a(641),i=a(33);const s={class:"container mt-5"},r={class:"barramenu mb-4"},l={class:"margin_button d-flex justify-content-around"},c=["disabled"],d=["disabled"],p={class:"main-content"};function m(e,o,a,m,b,u){const h=(0,t.g2)("Paso1"),f=(0,t.g2)("Paso2"),v=(0,t.g2)("Paso3"),k=(0,t.g2)("Paso4");return(0,t.uX)(),(0,t.CE)("div",null,[o[5]||(o[5]=(0,t.Lk)("header",{class:"header text-center"},[(0,t.Lk)("div",{class:"container"},[(0,t.Lk)("h1",null,"Formulario de Varias Etapas")])],-1)),(0,t.Lk)("main",s,[(0,t.Lk)("div",r,[o[4]||(o[4]=(0,t.Lk)("p",null,"Mereth Aldaron Enyalië - Formulario de Inscripción",-1)),(0,t.Lk)("div",l,[(0,t.Lk)("button",{class:"btn btn-primary",onClick:o[0]||(o[0]=(...e)=>u.inicioGrupos&&u.inicioGrupos(...e)),disabled:b.isDisabled,style:(0,i.Tr)({cursor:b.isDisabled?"not-allowed":"pointer"})}," Inscripción General ",12,c),(0,t.Lk)("button",{class:"btn btn-secondary",onClick:o[1]||(o[1]=(...e)=>u.inicioIndividual&&u.inicioIndividual(...e)),disabled:b.isDisabled,style:(0,i.Tr)({cursor:b.isDisabled?"not-allowed":"pointer"})}," Inscripción Aleatoria ",12,d),(0,t.bo)((0,t.Lk)("button",{class:"btn btn-warning",onClick:o[2]||(o[2]=(...o)=>e.inicioListaEspera&&e.inicioListaEspera(...o))}," Apuntarse a la lista de espera ",512),[[n.aG,b.showListaEspera]]),(0,t.Lk)("button",{class:"btn btn-danger",onClick:o[3]||(o[3]=(...o)=>e.reiniciarProceso&&e.reiniciarProceso(...o))}," Reiniciar proceso ")])]),(0,t.Lk)("div",p,[1!==b.currentStep||b.isLoading?(0,t.Q3)("",!0):((0,t.uX)(),(0,t.Wv)(h,{key:0,formData:b.formData,baseUrl:b.formData.baseUrl,onNextStep:u.handleNextStep,ref:"paso1Component"},null,8,["formData","baseUrl","onNextStep"])),2===b.currentStep?((0,t.uX)(),(0,t.Wv)(f,{key:1,currentStep:b.currentStep,formData:b.formData,baseUrl:e.baseUrl,mensaje:e.mensaje,onNextStep:u.handleNextStep,onPrevStep:u.handlePrevStep,ref:"paso2Component"},null,8,["currentStep","formData","baseUrl","mensaje","onNextStep","onPrevStep"])):(0,t.Q3)("",!0),3===b.currentStep?((0,t.uX)(),(0,t.Wv)(v,{key:2,inscritos:b.inscritos,currentStep:"3",formData:b.formData,baseUrl:e.baseUrl,mensaje:e.mensaje,onNextStep:u.handleNextStep,onPrevStep:u.handlePrevStep,ref:"paso3Component"},null,8,["inscritos","formData","baseUrl","mensaje","onNextStep","onPrevStep"])):(0,t.Q3)("",!0),4!==b.currentStep||b.isLoading?(0,t.Q3)("",!0):((0,t.uX)(),(0,t.Wv)(k,{key:3,formData:b.formData,onPrevStep:u.handlePrevStep,onSubmitForm:e.handleSubmit},null,8,["formData","onPrevStep","onSubmitForm"]))])]),o[6]||(o[6]=(0,t.Lk)("footer",{class:"footer text-center mt-5"},[(0,t.Lk)("div",{class:"container"},[(0,t.Lk)("p",null,"© 2024 Formulario de Varias Etapas. Todos los derechos reservados.")])],-1))])}const b={class:"container"},u={class:"row"},h={class:"col-md-6"},f=["innerHTML"],v={key:0,class:"col-md-6"};function k(e,o,a,n,s,r){return(0,t.uX)(),(0,t.CE)("div",b,[o[4]||(o[4]=(0,t.Lk)("h2",{class:"mb-4"},"Paso 1",-1)),(0,t.Lk)("div",u,[(0,t.Lk)("div",h,[(0,t.Lk)("div",{ref:"paso1",innerHTML:s.comboHabitaciones},null,8,f),(0,t.Lk)("button",{class:"btn btn-primary mt-3",onClick:o[0]||(o[0]=(...e)=>r.nextStep&&r.nextStep(...e))},"Siguiente")]),s.selectedHabitacion?((0,t.uX)(),(0,t.CE)("div",v,[o[3]||(o[3]=(0,t.Lk)("h3",null,"Detalles de la Habitación Seleccionada",-1)),(0,t.Lk)("p",null,[o[1]||(o[1]=(0,t.Lk)("strong",null,"Capacidad:",-1)),(0,t.eW)(" "+(0,i.v_)(s.selectedHabitacion.capacidad),1)]),(0,t.Lk)("p",null,[o[2]||(o[2]=(0,t.Lk)("strong",null,"Camas:",-1)),(0,t.eW)(" "+(0,i.v_)(s.selectedHabitacion.camas),1)])])):(0,t.Q3)("",!0)])])}var S=a(335);const g={props:{formData:{type:Object,required:!0},baseUrl:{type:String,required:!0}},data(){return{comboHabitaciones:null,selectedHabitacion:null}},methods:{cargarComboPaso1(e){console.log("Cargando habitaciones desde:",this.baseUrl),S.A.get(`${this.baseUrl}/Valimar/GetComboHabitacionesDisponibles`).then((o=>{console.log("Respuesta del servidor (habitaciones):",o.data);const a=new DOMParser,n=a.parseFromString(o.data,"text/html"),t=n.querySelectorAll('input[type="button"]');t.forEach((e=>e.remove())),this.comboHabitaciones=n.body.innerHTML,e&&e()})).catch((e=>{console.error("Error al cargar el combo de habitaciones:",e.response||e.message)}))},handleSelection(e){const o=e.target.value;if(o.includes(",")){const[e,a]=o.split(",");this.selectedHabitacion={capacidad:e,camas:a}}else console.error("Formato inesperado para la opción seleccionada:",o)},nextStep(){const e=this.$refs.paso1.querySelector("select");if(e){const o=e.value;if(!o.includes(","))return void console.error("Formato de la opción seleccionada no válido:",o);const[a,n]=o.split(",");console.log("Capacidad seleccionada:",a),console.log("Camas seleccionadas:",n),this.$emit("nextStep",{comboHabitaciones:o,idhabitacion:this.selectedHabitacion.idhabitacion})}else console.error("El elemento <select> no está disponible.")}},mounted(){console.log("Montando el componente Paso1..."),this.cargarComboPaso1((()=>{this.$nextTick((()=>{const e=this.$refs.paso1.querySelector("select");e?e.addEventListener("change",this.handleSelection):console.error("No se encontró el elemento <select> después de cargar el combo.")}))}))}};var L=a(262);const C=(0,L.A)(g,[["render",k]]),D=C,E=["innerHTML"],P={key:1,class:"alert alert-warning",role:"alert"};function y(e,o,a,n,s,r){return(0,t.uX)(),(0,t.CE)(t.FK,null,[o[1]||(o[1]=(0,t.Lk)("h2",{class:"mb-4"},"Paso 2 - Bloqueo de Habitación",-1)),s.mensaje&&!s.error?((0,t.uX)(),(0,t.CE)("div",{key:0,class:"alert alert-success",role:"alert",innerHTML:s.mensaje},null,8,E)):(0,t.Q3)("",!0),s.error?((0,t.uX)(),(0,t.CE)("div",P,(0,i.v_)(s.error),1)):(0,t.Q3)("",!0),s.habitacionBloqueada?((0,t.uX)(),(0,t.CE)("button",{key:2,class:"btn btn-primary mt-3",onClick:o[0]||(o[0]=(...e)=>r.nextStep&&r.nextStep(...e))}," Continuar al Paso 3 ")):(0,t.Q3)("",!0)],64)}const _={name:"Paso2",props:{currentStep:{type:Number,required:!0}},data(){return{mensaje:"",error:"",habitacionBloqueada:!1}},mounted(){this.mensaje="",this.error=""},methods:{nextStep(){console.log("Botón 'Continuar al Paso 3' clicado."),this.$emit("nextStep")},mostrarMensaje(e){e?(console.log("Mensaje recibido:",e),this.mensaje=e,this.error="",console.warn("LA HABITACIÓN HA SIDO BLOQUEADA"),this.habitacionBloqueada=!0,console.log("Habitación bloqueada:",this.habitacionBloqueada)):console.warn("El mensaje proporcionado está vacío o no es válido.")},mostrarError(e){e?(console.log("Error recibido:",e),this.error=e,this.mensaje="",this.habitacionBloqueada=!1,console.log("Habitación bloqueada:",this.habitacionBloqueada)):console.warn("El mensaje de error proporcionado está vacío o no es válido.")}},watch:{mensaje(e){e&&(console.log("Watcher detectó cambio en mensaje: ",e),this.habitacionBloqueada=!0)},error(e){e&&(console.log("Watcher detectó cambio en error: ",e),this.habitacionBloqueada=!1)}}},A=(0,L.A)(_,[["render",y]]),I=A,O={class:"container"},H={id:"paso3Datos",class:"row"},$={class:"card mb-4"},x={class:"card-body"},U={class:"row"},w={class:"col-md-6 mb-3"},j=["for"],q=["id","onUpdate:modelValue"],N={class:"col-md-6 mb-3"},V=["for"],B=["id","onUpdate:modelValue"],T={class:"row"},M={class:"col-md-6 mb-3"},X=["for"],F=["id","onUpdate:modelValue"],Q={class:"col-md-6 mb-3"},R=["for"],G=["id","onUpdate:modelValue"],W={class:"mb-3"},z=["for"],J=["id","onUpdate:modelValue"],K={class:"form-check mb-3"},Y=["id","onUpdate:modelValue"],Z=["for"],ee={key:0,class:"mt-3"},oe=["id","onClick"],ae=["for"],ne={key:0,class:"mt-2"},te=["id","onUpdate:modelValue"],ie={key:1,class:"form-check mt-3"},se=["id","onUpdate:modelValue"],re=["for"],le=["disabled"];function ce(e,o,a,s,r,l){return(0,t.uX)(),(0,t.CE)("div",null,[(0,t.Lk)("div",O,[(0,t.Lk)("div",H,[((0,t.uX)(!0),(0,t.CE)(t.FK,null,(0,t.pI)(a.inscritos,((e,a)=>((0,t.uX)(),(0,t.CE)("div",{class:"col-12",key:a},[(0,t.Lk)("div",$,[(0,t.Lk)("div",x,[(0,t.Lk)("h2",null,"Datos del inscrito número "+(0,i.v_)(a+1),1),(0,t.Lk)("div",U,[(0,t.Lk)("div",w,[(0,t.Lk)("label",{for:"nombre_"+a,class:"form-label"},"Nombre",8,j),(0,t.bo)((0,t.Lk)("input",{type:"text",id:"nombre_"+a,class:"form-control","onUpdate:modelValue":o=>e.nombre=o,onInput:o[0]||(o[0]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,q),[[n.Jo,e.nombre]])]),(0,t.Lk)("div",N,[(0,t.Lk)("label",{for:"pseudonimo_"+a,class:"form-label"},"Pseudónimo (opcional)",8,V),(0,t.bo)((0,t.Lk)("input",{type:"text",id:"pseudonimo_"+a,class:"form-control","onUpdate:modelValue":o=>e.pseudonimo=o,onInput:o[1]||(o[1]=(...e)=>l.disableEnviar&&l.disableEnviar(...e))},null,40,B),[[n.Jo,e.pseudonimo]])])]),(0,t.Lk)("div",T,[(0,t.Lk)("div",M,[(0,t.Lk)("label",{for:"email_"+a,class:"form-label"},"Email",8,X),(0,t.bo)((0,t.Lk)("input",{type:"email",id:"email_"+a,class:"form-control","onUpdate:modelValue":o=>e.email=o,onInput:o[2]||(o[2]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,F),[[n.Jo,e.email]])]),(0,t.Lk)("div",Q,[(0,t.Lk)("label",{for:"telefono_"+a,class:"form-label"},"Teléfono",8,R),(0,t.bo)((0,t.Lk)("input",{type:"tel",id:"telefono_"+a,class:"form-control","onUpdate:modelValue":o=>e.telefono=o,onInput:o[3]||(o[3]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,G),[[n.Jo,e.telefono]])])]),(0,t.Lk)("div",W,[(0,t.Lk)("label",{for:"iden_"+a,class:"form-label"},"NIF/NIE/Pasaporte",8,z),(0,t.bo)((0,t.Lk)("input",{type:"text",id:"iden_"+a,class:"form-control","onUpdate:modelValue":o=>e.nif=o,onInput:o[4]||(o[4]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,J),[[n.Jo,e.nif]])]),(0,t.Lk)("div",K,[(0,t.bo)((0,t.Lk)("input",{type:"checkbox",id:"lopd_"+a,class:"form-check-input","onUpdate:modelValue":o=>e.aceptaCondiciones=o,onChange:o[5]||(o[5]=(...e)=>l.disableEnviar&&l.disableEnviar(...e))},null,40,Y),[[n.lH,e.aceptaCondiciones]]),(0,t.Lk)("label",{class:"form-check-label",for:"lopd_"+a},o[8]||(o[8]=[(0,t.eW)(" He leído y acepto las "),(0,t.Lk)("a",{href:"terminos.html",target:"_blank"},"condiciones de inscripción al evento",-1),(0,t.eW)(" (incluidas las referentes a uso de mi imagen) ")]),8,Z)]),0===a?((0,t.uX)(),(0,t.CE)("div",ee,[(0,t.Lk)("input",{type:"checkbox",id:"con_bebes_"+a,onClick:e=>l.toggleBebe(a),class:"form-check-input"},null,8,oe),(0,t.Lk)("label",{for:"con_bebes_"+a,class:"form-check-label"}," Voy con menor/es de 2 años (no ocupan plaza) ",8,ae),r.showBebe[a]?((0,t.uX)(),(0,t.CE)("div",ne,[(0,t.bo)((0,t.Lk)("select",{id:"fecha_bebe_"+a,class:"form-select","onUpdate:modelValue":o=>e.fechaBebe=o},o[9]||(o[9]=[(0,t.Lk)("option",{value:"0"},"(Año de nacimiento del menor)",-1),(0,t.Lk)("option",{value:"2022"},"2022",-1),(0,t.Lk)("option",{value:"2023"},"2023",-1),(0,t.Lk)("option",{value:"2024"},"2024",-1)]),8,te),[[n.u1,e.fechaBebe]]),(0,t.Lk)("button",{type:"button",class:"btn btn-secondary mt-2",onClick:o[6]||(o[6]=(...e)=>l.addInscrito&&l.addInscrito(...e))},"Añadir inscrito")])):(0,t.Q3)("",!0)])):(0,t.Q3)("",!0),a>0?((0,t.uX)(),(0,t.CE)("div",ie,[(0,t.bo)((0,t.Lk)("input",{type:"checkbox",id:"es_menor_"+a,"onUpdate:modelValue":o=>e.menor=o,class:"form-check-input"},null,8,se),[[n.lH,e.menor]]),(0,t.Lk)("label",{for:"es_menor_"+a,class:"form-check-label"}," Menor entre 2 y 12 años ",8,re)])):(0,t.Q3)("",!0)])])])))),128))]),(0,t.Lk)("button",{class:"btn btn-primary",onClick:o[7]||(o[7]=(...e)=>l.nextStep&&l.nextStep(...e)),disabled:r.isEnviarDisabled},"Siguiente paso",8,le)])])}const de={name:"Paso3",props:{formData:{type:Object,required:!0},baseUrl:{type:String,required:!0},currentStep:{type:Number,required:!0},inscritos:{type:Array,required:!0}},data(){return{isEnviarDisabled:!0,showBebe:[]}},methods:{addInscrito(){this.inscritos.push({nombre:"",apellidos:"",pseudonimo:"",email:"",telefono:"",nif:"",aceptaCondiciones:!1,menor:!1,fechaBebe:null})},removeInscrito(e){e>0&&this.inscritos.splice(e,1)},toggleBebe(e){void 0===this.showBebe[e]&&(this.showBebe[e]=!1),this.showBebe[e]=!this.showBebe[e]},disableEnviar(){this.isEnviarDisabled=!0},nextStep(){this.$emit("nextStep")}}},pe=(0,L.A)(de,[["render",ce]]),me=pe;function be(e,o,a,n,i,s){return(0,t.uX)(),(0,t.CE)("div",null,[o[2]||(o[2]=(0,t.Lk)("h2",null,"Paso 4",-1)),o[3]||(o[3]=(0,t.Lk)("div",{id:"paso4"},null,-1)),(0,t.Lk)("button",{onClick:o[0]||(o[0]=(...e)=>s.prevStep&&s.prevStep(...e))},"Anterior"),(0,t.Lk)("button",{onClick:o[1]||(o[1]=(...e)=>s.submitForm&&s.submitForm(...e))},"Enviar")])}var ue=a(692),he=a.n(ue);const fe={props:["formData"],methods:{prevStep(){this.$emit("prevStep")},submitForm(){this.enviarInscripcion()},enviarInscripcion(){for(var e=this.formData.comboHabitaciones.split(",")[0],o=[],a=0;a<e;a++){var n={};n["iden"]=this.formData[`iden_${a+1}`],n["nombre"]=this.formData[`nombre_${a+1}`],n["apellidos"]=this.formData[`apellidos_${a+1}`],n["pseudonimo"]=this.formData[`pseudonimo_${a+1}`],n["email"]=this.formData[`email_${a+1}`],n["telefono"]=this.formData[`telefono_${a+1}`],n["lopd"]=this.formData[`lopd_${a+1}`],0==a?(n["menor"]=!1,n["con_bebes"]=this.formData[`fecha_bebe_${a+1}`]):(n["menor"]=this.formData[`es_menor_${a+1}`],n["con_bebes"]=0),o.push(n)}he().ajax({url:"/Valimar/RegistrarInscripcion",type:"POST",data:"habitacion="+this.formData.idhabitacion+"&datos="+JSON.stringify(o),success:function(e,o){he()("#paso3").hide(),he()("#paso2").hide(),he()("#paso4").show(),he()("#paso4").html(e)}})}}},ve=(0,L.A)(fe,[["render",be]]),ke=ve,Se=["disabled"],ge=["disabled"];function Le(e,o,a,n,s,r){return a.visible?((0,t.uX)(),(0,t.CE)("div",{key:0,class:(0,i.C4)({disabled:a.isDisabled})},[(0,t.RG)(e.$slots,"default",{},void 0,!0),(0,t.Lk)("button",{disabled:a.isDisabled,onClick:o[0]||(o[0]=(...e)=>r.validar&&r.validar(...e))},"Validar",8,Se),(0,t.Lk)("button",{disabled:a.isDisabled,onClick:o[1]||(o[1]=(...e)=>r.enviar&&r.enviar(...e))},"Enviar",8,ge)],2)):(0,t.Q3)("",!0)}const Ce={props:{visible:Boolean,isDisabled:Boolean},methods:{validar(){this.$emit("validar")},enviar(){this.$emit("enviar")}}},De=(0,L.A)(Ce,[["render",Le],["__scopeId","data-v-612bf9c6"]]),Ee=De,Pe={name:"App",components:{Paso1:D,Paso2:I,Paso3:me,Paso4:ke,ControlComponent:Ee},data(){return{currentStep:1,formData:{comboHabitaciones:"",idhabitacion:"",baseUrl:"http://127.0.0.1:8080",mensaje:""},isDisabled:!1,showListaEspera:!1,isLoading:!1,showInfo:!0,showPaso3:!1,showPaso3Individual:!1,showPaso3EsperaDatos:!1,inscritos:[]}},watch:{currentStep(e){console.log(`[WATCH] Cambio de paso: Paso ${e}`)}},methods:{handleNextStep(e){if(console.log("[NEXT STEP] Datos del paso siguiente recibidos:",e),this.formData={...this.formData,...e},console.log("Step ->"+this.currentStep),console.log("formData:",this.formData),1===this.currentStep)this.lanzarPaso2();else if(2===this.currentStep){console.log("[NEXT STEP] Avanzando al paso 3..."),console.log("comboHabitaciones:",this.formData.comboHabitaciones),console.log("idHabitacion:",this.formData.idhabitacion);const e=this.formData.comboHabitaciones?parseInt(this.formData.comboHabitaciones.split(",")[0],10):void 0,o=this.formData.idhabitacion;e&&o?this.generarPaso3(e,o):console.error("Faltan datos para generar los inscritos")}else this.currentStep<4&&this.currentStep++},handlePrevStep(){this.currentStep>1&&(console.log("[PREV STEP] Retrocediendo al paso anterior."),this.currentStep--)},inicioGrupos(){const e=confirm("Este tipo de inscripción es para habitaciones INDIVIDUALES NO COMPARTIDAS o GRUPOS COMPLETOS...");e&&(console.log("[INICIO GRUPOS] Iniciando inscripción general."),this.lanzarPaso1(),this.isDisabled=!0)},inicioIndividual(){const e=confirm("Este tipo de inscripción es para una inscripción individual en una habitación COMPARTIDA...");e&&(console.log("[INICIO INDIVIDUAL] Iniciando inscripción individual."),this.lanzarPaso1individual(),this.isDisabled=!0)},cargarComboPaso1(){console.log("[PASO 1] Solicitando habitaciones disponibles..."),this.isLoading=!0,S.A.get(`${this.formData.baseUrl}/Valimar/GetComboHabitacionesDisponibles`).then((e=>{console.log("[PASO 1] Habitaciones disponibles recibidas:",e.data);const o=this.$refs.paso1Component;o?(o.$refs.paso1.innerHTML=e.data,o.$refs.paso1.style.display="block"):console.error("[PASO 1] Componente Paso1 no encontrado.")})).catch((e=>{console.error("[PASO 1] Error al obtener habitaciones:",e)})).finally((()=>{this.isLoading=!1}))},lanzarPaso2(){console.log("[PASO 2] Iniciando solicitud para bloquear habitación..."),this.formData.comboHabitaciones?(this.currentStep=2,this.isLoading=!0,S.A.post(`${this.formData.baseUrl}/Valimar/BloquearHabitacion`,{capacidad:this.formData.comboHabitaciones.split(",")[0],camas:this.formData.comboHabitaciones.split(",")[1]}).then((e=>{console.log("[PASO 2] Respuesta recibida:",e.data);const o=this.$refs.paso2Component;if(console.log("[PASO 2] Comprobando valores:"),console.log("paso2Component:",o),console.log("response.data:",e.data),o&&e.data&&""!==e.data.trim()){this.formData.idhabitacion=e.data;const a=`\n          <div class="alert alert-info" role="alert">\n            <h4 class="alert-heading">Habitación Bloqueada</h4>\n            <p>Habitación bloqueada temporalmente: <strong>${e.data}</strong>.</p>\n            <hr>\n            <p class="mb-0">Dispone de 10 minutos para completar la reserva.</p>\n          </div>`;o.mostrarMensaje(a)}else console.warn("[PASO 2] No se pudo bloquear habitación o datos incompletos."),this.$refs.paso2Component&&"function"===typeof this.$refs.paso2Component.mostrarError?this.$refs.paso2Component.mostrarError("No se pudo bloquear la habitación. Inténtelo de nuevo."):console.error("[PASO 2] No se pudo encontrar el método mostrarError en el componente.")})).catch((e=>{console.error("[PASO 2] Error al bloquear habitación:",e),this.$refs.paso2Component&&"function"===typeof this.$refs.paso2Component.mostrarError&&this.$refs.paso2Component.mostrarError("Error al conectar con el servicio. Inténtelo más tarde.")}))):console.error("[PASO 2] comboHabitaciones está vacío. Abortando.")},generarPaso3(e,o){if(console.log(`[PASO 3] Generando paso 3 con capacidad: ${e}, idHabitacion: ${o}`),!e||!o)return console.error(`[PASO 3] Datos incompletos. Capacidad: ${e}, idHabitacion: ${o}. Abandonando proceso.`),void this.$emit("mostrarError","Capacidad o ID de habitación no especificados.");if(e=parseInt(e,10),isNaN(e))return console.error(`[PASO 3] Capacidad no es un número válido: ${e}. Abandonando proceso.`),void this.$emit("mostrarError","Capacidad debe ser un número válido.");3!==this.currentStep&&(this.currentStep=3,console.log(`[PASO 3] Cambio a currentStep: ${this.currentStep}`)),console.log("Reseteando array inscritos..."),this.inscritos=this.inscritos.slice(0,e);for(let a=0;a<e;a++)this.inscritos[a]||(console.log(`[PASO 3] Creamos usuario: ${a}`),Vue.set(this.inscritos,a,{nombre:"",apellidos:"",pseudonimo:"",email:"",telefono:"",nif:"",aceptaCondiciones:!1,menor:a>0,conBebes:0!==a&&null,fechaBebe:0===a?"":null}));this.inscritos.length>e&&(this.inscritos.splice(e),console.log(`[PASO 3] Ajustamos tamaño de inscritos a: ${e}`)),this.$nextTick((()=>{console.log("La vista debería haberse actualizado ahora para el paso 3."),console.log("Estado actual de inscritos:",JSON.stringify(this.inscritos,null,2))}))}}},ye=(0,L.A)(Pe,[["render",m],["__scopeId","data-v-cbf79b3a"]]),_e=ye;document.addEventListener("DOMContentLoaded",(()=>{const e=document.querySelector("#app");console.log(e),e&&!e.__vue_app__?(console.log("Montando la aplicación..."),(0,n.Ef)(_e).mount("#app")):console.log("La aplicación ya está montada.")}))}},o={};function a(n){var t=o[n];if(void 0!==t)return t.exports;var i=o[n]={exports:{}};return e[n].call(i.exports,i,i.exports,a),i.exports}a.m=e,(()=>{var e=[];a.O=(o,n,t,i)=>{if(!n){var s=1/0;for(d=0;d<e.length;d++){for(var[n,t,i]=e[d],r=!0,l=0;l<n.length;l++)(!1&i||s>=i)&&Object.keys(a.O).every((e=>a.O[e](n[l])))?n.splice(l--,1):(r=!1,i<s&&(s=i));if(r){e.splice(d--,1);var c=t();void 0!==c&&(o=c)}}return o}i=i||0;for(var d=e.length;d>0&&e[d-1][2]>i;d--)e[d]=e[d-1];e[d]=[n,t,i]}})(),(()=>{a.n=e=>{var o=e&&e.__esModule?()=>e["default"]:()=>e;return a.d(o,{a:o}),o}})(),(()=>{a.d=(e,o)=>{for(var n in o)a.o(o,n)&&!a.o(e,n)&&Object.defineProperty(e,n,{enumerable:!0,get:o[n]})}})(),(()=>{a.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()})(),(()=>{a.o=(e,o)=>Object.prototype.hasOwnProperty.call(e,o)})(),(()=>{a.r=e=>{"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}})(),(()=>{var e={524:0};a.O.j=o=>0===e[o];var o=(o,n)=>{var t,i,[s,r,l]=n,c=0;if(s.some((o=>0!==e[o]))){for(t in r)a.o(r,t)&&(a.m[t]=r[t]);if(l)var d=l(a)}for(o&&o(n);c<s.length;c++)i=s[c],a.o(e,i)&&e[i]&&e[i][0](),e[i]=0;return a.O(d)},n=self["webpackChunkValimarFrontal"]=self["webpackChunkValimarFrontal"]||[];n.forEach(o.bind(null,0)),n.push=o.bind(null,n.push.bind(n))})();var n=a.O(void 0,[504],(()=>a(442)));n=a.O(n)})();
//# sourceMappingURL=app.d22dd37e.js.map