(()=>{"use strict";var e={953:(e,o,a)=>{var n=a(751),t=a(641),i=a(33);const s={class:"container mt-5"},r={class:"barramenu mb-4"},l={class:"margin_button d-flex justify-content-around"},c=["disabled"],d=["disabled"],p={class:"main-content"};function b(e,o,a,b,m,u){const h=(0,t.g2)("Paso1"),f=(0,t.g2)("Paso2"),v=(0,t.g2)("Paso3"),S=(0,t.g2)("Paso4");return(0,t.uX)(),(0,t.CE)("div",null,[o[5]||(o[5]=(0,t.Lk)("header",{class:"header text-center"},[(0,t.Lk)("div",{class:"container"},[(0,t.Lk)("h1",null,"Formulario de Varias Etapas")])],-1)),(0,t.Lk)("main",s,[(0,t.Lk)("div",r,[o[4]||(o[4]=(0,t.Lk)("p",null,"Mereth Aldaron Enyalië - Formulario de Inscripción",-1)),(0,t.Lk)("div",l,[(0,t.Lk)("button",{class:"btn btn-primary",onClick:o[0]||(o[0]=(...e)=>u.inicioGrupos&&u.inicioGrupos(...e)),disabled:m.isDisabled,style:(0,i.Tr)({cursor:m.isDisabled?"not-allowed":"pointer"})}," Inscripción General ",12,c),(0,t.Lk)("button",{class:"btn btn-secondary",onClick:o[1]||(o[1]=(...e)=>u.inicioIndividual&&u.inicioIndividual(...e)),disabled:m.isDisabled,style:(0,i.Tr)({cursor:m.isDisabled?"not-allowed":"pointer"})}," Inscripción Aleatoria ",12,d),(0,t.bo)((0,t.Lk)("button",{class:"btn btn-warning",onClick:o[2]||(o[2]=(...o)=>e.inicioListaEspera&&e.inicioListaEspera(...o))}," Apuntarse a la lista de espera ",512),[[n.aG,m.showListaEspera]]),(0,t.Lk)("button",{class:"btn btn-danger",onClick:o[3]||(o[3]=(...o)=>e.reiniciarProceso&&e.reiniciarProceso(...o))}," Reiniciar proceso ")])]),(0,t.Lk)("div",p,[1!==m.currentStep||m.isLoading?(0,t.Q3)("",!0):((0,t.uX)(),(0,t.Wv)(h,{key:0,formData:m.formData,baseUrl:m.formData.baseUrl,onNextStep:u.handleNextStep,ref:"paso1Component"},null,8,["formData","baseUrl","onNextStep"])),2===m.currentStep?((0,t.uX)(),(0,t.Wv)(f,{key:1,currentStep:m.currentStep,formData:m.formData,baseUrl:e.baseUrl,mensaje:e.mensaje,onNextStep:u.handleNextStep,onPrevStep:u.handlePrevStep,ref:"paso2Component"},null,8,["currentStep","formData","baseUrl","mensaje","onNextStep","onPrevStep"])):(0,t.Q3)("",!0),3===m.currentStep?((0,t.uX)(),(0,t.Wv)(v,{key:2,currentStep:3,formData:m.formData,baseUrl:e.baseUrl,mensaje:e.mensaje,onNextStep:u.handleNextStep,onPrevStep:u.handlePrevStep,ref:"paso3Component"},null,8,["formData","baseUrl","mensaje","onNextStep","onPrevStep"])):(0,t.Q3)("",!0),4!==m.currentStep||m.isLoading?(0,t.Q3)("",!0):((0,t.uX)(),(0,t.Wv)(S,{key:3,formData:m.formData,onPrevStep:u.handlePrevStep,onSubmitForm:e.handleSubmit},null,8,["formData","onPrevStep","onSubmitForm"]))])]),o[6]||(o[6]=(0,t.Lk)("footer",{class:"footer text-center mt-5"},[(0,t.Lk)("div",{class:"container"},[(0,t.Lk)("p",null,"© 2024 Formulario de Varias Etapas. Todos los derechos reservados.")])],-1))])}const m={class:"container"},u={class:"row"},h={class:"col-md-6"},f=["innerHTML"],v={key:0,class:"col-md-6"};function S(e,o,a,n,s,r){return(0,t.uX)(),(0,t.CE)("div",m,[o[4]||(o[4]=(0,t.Lk)("h2",{class:"mb-4"},"Paso 1",-1)),(0,t.Lk)("div",u,[(0,t.Lk)("div",h,[(0,t.Lk)("div",{ref:"paso1",innerHTML:s.comboHabitaciones},null,8,f),(0,t.Lk)("button",{class:"btn btn-primary mt-3",onClick:o[0]||(o[0]=(...e)=>r.nextStep&&r.nextStep(...e))},"Siguiente")]),s.selectedHabitacion?((0,t.uX)(),(0,t.CE)("div",v,[o[3]||(o[3]=(0,t.Lk)("h3",null,"Detalles de la Habitación Seleccionada",-1)),(0,t.Lk)("p",null,[o[1]||(o[1]=(0,t.Lk)("strong",null,"Capacidad:",-1)),(0,t.eW)(" "+(0,i.v_)(s.selectedHabitacion.capacidad),1)]),(0,t.Lk)("p",null,[o[2]||(o[2]=(0,t.Lk)("strong",null,"Camas:",-1)),(0,t.eW)(" "+(0,i.v_)(s.selectedHabitacion.camas),1)])])):(0,t.Q3)("",!0)])])}var k=a(335);const g={props:{formData:{type:Object,required:!0},baseUrl:{type:String,required:!0}},data(){return{comboHabitaciones:null,selectedHabitacion:null}},methods:{cargarComboPaso1(e){console.log("Cargando habitaciones desde:",this.baseUrl),k.A.get(`${this.baseUrl}/Valimar/GetComboHabitacionesDisponibles`).then((o=>{console.log("Respuesta del servidor (habitaciones):",o.data);const a=new DOMParser,n=a.parseFromString(o.data,"text/html"),t=n.querySelectorAll('input[type="button"]');t.forEach((e=>e.remove())),this.comboHabitaciones=n.body.innerHTML,e&&e()})).catch((e=>{console.error("Error al cargar el combo de habitaciones:",e.response||e.message)}))},handleSelection(e){const o=e.target.value;if(o.includes(",")){const[e,a]=o.split(",");this.selectedHabitacion={capacidad:e,camas:a}}else console.error("Formato inesperado para la opción seleccionada:",o)},nextStep(){const e=this.$refs.paso1.querySelector("select");if(e){const o=e.value;if(!o.includes(","))return void console.error("Formato de la opción seleccionada no válido:",o);const[a,n]=o.split(",");console.log("Capacidad seleccionada:",a),console.log("Camas seleccionadas:",n),this.$emit("nextStep",{comboHabitaciones:o,idhabitacion:this.selectedHabitacion.idhabitacion})}else console.error("El elemento <select> no está disponible.")}},mounted(){console.log("Montando el componente Paso1..."),this.cargarComboPaso1((()=>{this.$nextTick((()=>{const e=this.$refs.paso1.querySelector("select");e?e.addEventListener("change",this.handleSelection):console.error("No se encontró el elemento <select> después de cargar el combo.")}))}))}};var L=a(262);const E=(0,L.A)(g,[["render",S]]),C=E,D=["innerHTML"],y={key:1,class:"alert alert-warning",role:"alert"};function P(e,o,a,n,s,r){return(0,t.uX)(),(0,t.CE)(t.FK,null,[o[1]||(o[1]=(0,t.Lk)("h2",{class:"mb-4"},"Paso 2 - Bloqueo de Habitación",-1)),s.mensaje&&!s.error?((0,t.uX)(),(0,t.CE)("div",{key:0,class:"alert alert-success",role:"alert",innerHTML:s.mensaje},null,8,D)):(0,t.Q3)("",!0),s.error?((0,t.uX)(),(0,t.CE)("div",y,(0,i.v_)(s.error),1)):(0,t.Q3)("",!0),s.habitacionBloqueada?((0,t.uX)(),(0,t.CE)("button",{key:2,class:"btn btn-primary mt-3",onClick:o[0]||(o[0]=(...e)=>r.nextStep&&r.nextStep(...e))}," Continuar al Paso 3 ")):(0,t.Q3)("",!0)],64)}const _={name:"Paso2",props:{currentStep:{type:Number,required:!0}},data(){return{mensaje:"",error:"",habitacionBloqueada:!1}},mounted(){this.mensaje="",this.error=""},methods:{nextStep(){console.log("Botón 'Continuar al Paso 3' clicado."),this.$emit("nextStep")},mostrarMensaje(e){e?(console.log("Mensaje recibido:",e),this.mensaje=e,this.error="",console.warn("LA HABITACIÓN HA SIDO BLOQUEADA"),this.habitacionBloqueada=!0,console.log("Habitación bloqueada:",this.habitacionBloqueada)):console.warn("El mensaje proporcionado está vacío o no es válido.")},mostrarError(e){e?(console.log("Error recibido:",e),this.error=e,this.mensaje="",this.habitacionBloqueada=!1,console.log("Habitación bloqueada:",this.habitacionBloqueada)):console.warn("El mensaje de error proporcionado está vacío o no es válido.")}},watch:{mensaje(e){e&&(console.log("Watcher detectó cambio en mensaje: ",e),this.habitacionBloqueada=!0)},error(e){e&&(console.log("Watcher detectó cambio en error: ",e),this.habitacionBloqueada=!1)}}},A=(0,L.A)(_,[["render",P]]),I=A,$={key:0,class:"container"},O={id:"paso3Datos",class:"row"},H={class:"card mb-4"},x={class:"card-body"},U={class:"mb-3"},w=["for"],j=["id","onUpdate:modelValue"],q={class:"mb-3"},N=["for"],V=["id","onUpdate:modelValue"],T={class:"mb-3"},B=["for"],M=["id","onUpdate:modelValue"],X={class:"mb-3"},F=["for"],Q=["id","onUpdate:modelValue"],R={class:"mb-3"},G=["for"],W=["id","onUpdate:modelValue"],J={class:"mb-3"},z=["for"],K=["id","onUpdate:modelValue"],Y={class:"form-check"},Z=["id","onUpdate:modelValue"],ee=["for"],oe={key:0,class:"mt-3"},ae=["id","onClick"],ne=["for"],te={key:0,class:"mt-2"},ie=["id","onUpdate:modelValue"],se={key:1,class:"mt-3"},re=["id","onUpdate:modelValue"],le=["for"],ce=["disabled"];function de(e,o,a,s,r,l){return(0,t.uX)(),(0,t.CE)("div",null,[3===a.currentStep?((0,t.uX)(),(0,t.CE)("div",$,[o[10]||(o[10]=(0,t.Lk)("p",null,"¡Estás en el paso 3!",-1)),(0,t.Lk)("div",O,[((0,t.uX)(!0),(0,t.CE)(t.FK,null,(0,t.pI)(a.inscritos,((e,a)=>((0,t.uX)(),(0,t.CE)("div",{class:"col-12",key:a},[(0,t.Lk)("div",H,[(0,t.Lk)("div",x,[(0,t.Lk)("h2",null,"Datos del inscrito número "+(0,i.v_)(a+1),1),(0,t.Lk)("div",U,[(0,t.Lk)("label",{for:"nombre_"+a,class:"form-label"},"Nombre",8,w),(0,t.bo)((0,t.Lk)("input",{type:"text",id:"nombre_"+a,class:"form-control","onUpdate:modelValue":o=>e.nombre=o,onInput:o[0]||(o[0]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,j),[[n.Jo,e.nombre]])]),(0,t.Lk)("div",q,[(0,t.Lk)("label",{for:"apellidos_"+a,class:"form-label"},"Apellidos",8,N),(0,t.bo)((0,t.Lk)("input",{type:"text",id:"apellidos_"+a,class:"form-control","onUpdate:modelValue":o=>e.apellidos=o,onInput:o[1]||(o[1]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,V),[[n.Jo,e.apellidos]])]),(0,t.Lk)("div",T,[(0,t.Lk)("label",{for:"pseudonimo_"+a,class:"form-label"},"Pseudónimo (opcional)",8,B),(0,t.bo)((0,t.Lk)("input",{type:"text",id:"pseudonimo_"+a,class:"form-control","onUpdate:modelValue":o=>e.pseudonimo=o,onInput:o[2]||(o[2]=(...e)=>l.disableEnviar&&l.disableEnviar(...e))},null,40,M),[[n.Jo,e.pseudonimo]])]),(0,t.Lk)("div",X,[(0,t.Lk)("label",{for:"email_"+a,class:"form-label"},"Email",8,F),(0,t.bo)((0,t.Lk)("input",{type:"email",id:"email_"+a,class:"form-control","onUpdate:modelValue":o=>e.email=o,onInput:o[3]||(o[3]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,Q),[[n.Jo,e.email]])]),(0,t.Lk)("div",R,[(0,t.Lk)("label",{for:"telefono_"+a,class:"form-label"},"Teléfono",8,G),(0,t.bo)((0,t.Lk)("input",{type:"tel",id:"telefono_"+a,class:"form-control","onUpdate:modelValue":o=>e.telefono=o,onInput:o[4]||(o[4]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,W),[[n.Jo,e.telefono]])]),(0,t.Lk)("div",J,[(0,t.Lk)("label",{for:"iden_"+a,class:"form-label"},"NIF/NIE/Pasaporte",8,z),(0,t.bo)((0,t.Lk)("input",{type:"text",id:"iden_"+a,class:"form-control","onUpdate:modelValue":o=>e.nif=o,onInput:o[5]||(o[5]=(...e)=>l.disableEnviar&&l.disableEnviar(...e)),required:""},null,40,K),[[n.Jo,e.nif]])]),(0,t.Lk)("div",Y,[(0,t.bo)((0,t.Lk)("input",{type:"checkbox",id:"lopd_"+a,class:"form-check-input","onUpdate:modelValue":o=>e.aceptaCondiciones=o,onChange:o[6]||(o[6]=(...e)=>l.disableEnviar&&l.disableEnviar(...e))},null,40,Z),[[n.lH,e.aceptaCondiciones]]),(0,t.Lk)("label",{class:"form-check-label",for:"lopd_"+a},o[8]||(o[8]=[(0,t.eW)(" He leído y acepto las "),(0,t.Lk)("a",{href:"terminos.html",target:"_blank"},"condiciones de inscripción al evento",-1),(0,t.eW)(" (incluidas las referentes a uso de mi imagen) ")]),8,ee)]),0===a?((0,t.uX)(),(0,t.CE)("div",oe,[(0,t.Lk)("input",{type:"checkbox",id:"con_bebes_"+a,onClick:e=>l.toggleBebe(a),class:"form-check-input"},null,8,ae),(0,t.Lk)("label",{for:"con_bebes_"+a,class:"form-check-label"}," Voy con menor/es de 2 años (no ocupan plaza) ",8,ne),r.showBebe[a]?((0,t.uX)(),(0,t.CE)("div",te,[(0,t.bo)((0,t.Lk)("select",{id:"fecha_bebe_"+a,class:"form-select","onUpdate:modelValue":o=>e.fechaBebe=o},o[9]||(o[9]=[(0,t.Lk)("option",{value:"0"},"(Año de nacimiento del menor)",-1),(0,t.Lk)("option",{value:"2022"},"2022",-1),(0,t.Lk)("option",{value:"2023"},"2023",-1),(0,t.Lk)("option",{value:"2024"},"2024",-1)]),8,ie),[[n.u1,e.fechaBebe]])])):(0,t.Q3)("",!0)])):(0,t.Q3)("",!0),a>0?((0,t.uX)(),(0,t.CE)("div",se,[(0,t.bo)((0,t.Lk)("input",{type:"checkbox",id:"es_menor_"+a,"onUpdate:modelValue":o=>e.menor=o,class:"form-check-input"},null,8,re),[[n.lH,e.menor]]),(0,t.Lk)("label",{for:"es_menor_"+a,class:"form-check-label"}," Menor entre 2 y 12 años ",8,le)])):(0,t.Q3)("",!0)])])])))),128))]),(0,t.Lk)("button",{class:"btn btn-primary",onClick:o[7]||(o[7]=(...e)=>l.nextStep&&l.nextStep(...e)),disabled:r.isEnviarDisabled},"Siguiente paso",8,ce)])):(0,t.Q3)("",!0)])}var pe=a(756);const be={name:"Paso3",props:{formData:{type:Object,required:!0},baseUrl:{type:String,required:!0},currentStep:{type:Number,required:!0},inscritos:{type:Array,required:!0}},data(){return{isEnviarDisabled:!0,showBebe:[]}},mounted(){console.log("Mounted Paso3.vue, currentStep:",this.currentStep)},watch:{currentStep(e){console.log("Cambio de paso:",e),3===e&&console.log("Estamos en el paso 3")}},methods:{prevStep(){this.$emit("prevStep")},validar(){const e=[];this.inscritos.forEach(((o,a)=>{o.nombre||e.push(`Nombre ${a+1} en blanco.`),o.apellidos||e.push(`Apellidos ${a+1} en blanco.`),o.email||e.push(`Email ${a+1} en blanco.`),o.telefono||e.push(`Teléfono ${a+1} en blanco.`),o.nif||o.menor||e.push(`NIF/NIE ${a+1} en blanco.`),o.aceptaCondiciones||e.push(`Debe aceptar las condiciones de inscripción para el inscrito ${a+1}.`)})),e.length>0?alert(e.join("\n")):this.isEnviarDisabled=!1},async enviarInscripcion(){try{const e=await fetch(`${this.baseUrl}/Valimar/RegistrarInscripcion`,{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({habitacion:this.formData.idhabitacion,datos:this.inscritos})}),o=await e.text();document.getElementById("paso4").innerHTML=o}catch(e){console.error("Error al enviar la inscripción:",e),alert("Hubo un error al enviar la inscripción. Por favor, inténtalo de nuevo.")}},disableEnviar(){this.isEnviarDisabled=!0},toggleBebe(e){pe["default"].set(this.showBebe,e,!this.showBebe[e])},toggleMenor(e){pe["default"].set(this.inscritos[e],"menor",!this.inscritos[e].menor)},nextStep(){this.validar(),this.isEnviarDisabled||this.$emit("nextStep")}}},me=(0,L.A)(be,[["render",de]]),ue=me;function he(e,o,a,n,i,s){return(0,t.uX)(),(0,t.CE)("div",null,[o[2]||(o[2]=(0,t.Lk)("h2",null,"Paso 4",-1)),o[3]||(o[3]=(0,t.Lk)("div",{id:"paso4"},null,-1)),(0,t.Lk)("button",{onClick:o[0]||(o[0]=(...e)=>s.prevStep&&s.prevStep(...e))},"Anterior"),(0,t.Lk)("button",{onClick:o[1]||(o[1]=(...e)=>s.submitForm&&s.submitForm(...e))},"Enviar")])}var fe=a(692),ve=a.n(fe);const Se={props:["formData"],methods:{prevStep(){this.$emit("prevStep")},submitForm(){this.enviarInscripcion()},enviarInscripcion(){for(var e=this.formData.comboHabitaciones.split(",")[0],o=[],a=0;a<e;a++){var n={};n["iden"]=this.formData[`iden_${a+1}`],n["nombre"]=this.formData[`nombre_${a+1}`],n["apellidos"]=this.formData[`apellidos_${a+1}`],n["pseudonimo"]=this.formData[`pseudonimo_${a+1}`],n["email"]=this.formData[`email_${a+1}`],n["telefono"]=this.formData[`telefono_${a+1}`],n["lopd"]=this.formData[`lopd_${a+1}`],0==a?(n["menor"]=!1,n["con_bebes"]=this.formData[`fecha_bebe_${a+1}`]):(n["menor"]=this.formData[`es_menor_${a+1}`],n["con_bebes"]=0),o.push(n)}ve().ajax({url:"/Valimar/RegistrarInscripcion",type:"POST",data:"habitacion="+this.formData.idhabitacion+"&datos="+JSON.stringify(o),success:function(e,o){ve()("#paso3").hide(),ve()("#paso2").hide(),ve()("#paso4").show(),ve()("#paso4").html(e)}})}}},ke=(0,L.A)(Se,[["render",he]]),ge=ke,Le=["disabled"],Ee=["disabled"];function Ce(e,o,a,n,s,r){return a.visible?((0,t.uX)(),(0,t.CE)("div",{key:0,class:(0,i.C4)({disabled:a.isDisabled})},[(0,t.RG)(e.$slots,"default",{},void 0,!0),(0,t.Lk)("button",{disabled:a.isDisabled,onClick:o[0]||(o[0]=(...e)=>r.validar&&r.validar(...e))},"Validar",8,Le),(0,t.Lk)("button",{disabled:a.isDisabled,onClick:o[1]||(o[1]=(...e)=>r.enviar&&r.enviar(...e))},"Enviar",8,Ee)],2)):(0,t.Q3)("",!0)}const De={props:{visible:Boolean,isDisabled:Boolean},methods:{validar(){this.$emit("validar")},enviar(){this.$emit("enviar")}}},ye=(0,L.A)(De,[["render",Ce],["__scopeId","data-v-612bf9c6"]]),Pe=ye,_e={name:"App",components:{Paso1:C,Paso2:I,Paso3:ue,Paso4:ge,ControlComponent:Pe},data(){return{currentStep:1,formData:{comboHabitaciones:"",idhabitacion:"",baseUrl:"http://127.0.0.1:8080",mensaje:""},isDisabled:!1,showListaEspera:!1,isLoading:!1,showInfo:!0,showPaso3:!1,showPaso3Individual:!1,showPaso3EsperaDatos:!1,inscritos:[]}},watch:{currentStep(e){console.log(`[WATCH] Cambio de paso: Paso ${e}`)}},methods:{handleNextStep(e){if(console.log("[NEXT STEP] Datos del paso siguiente recibidos:",e),this.formData={...this.formData,...e},console.log("Step ->"+this.currentStep),console.log("formData:",this.formData),1===this.currentStep)this.lanzarPaso2();else if(2===this.currentStep){console.log("[NEXT STEP] Avanzando al paso 3..."),console.log("comboHabitaciones:",this.formData.comboHabitaciones),console.log("idHabitacion:",this.formData.idhabitacion);const e=this.formData.comboHabitaciones?parseInt(this.formData.comboHabitaciones.split(",")[0],10):void 0,o=this.formData.idhabitacion;e&&o?this.generarPaso3(e,o):console.error("Faltan datos para generar los inscritos")}else this.currentStep<4&&this.currentStep++},handlePrevStep(){this.currentStep>1&&(console.log("[PREV STEP] Retrocediendo al paso anterior."),this.currentStep--)},inicioGrupos(){const e=confirm("Este tipo de inscripción es para habitaciones INDIVIDUALES NO COMPARTIDAS o GRUPOS COMPLETOS...");e&&(console.log("[INICIO GRUPOS] Iniciando inscripción general."),this.lanzarPaso1(),this.isDisabled=!0)},inicioIndividual(){const e=confirm("Este tipo de inscripción es para una inscripción individual en una habitación COMPARTIDA...");e&&(console.log("[INICIO INDIVIDUAL] Iniciando inscripción individual."),this.lanzarPaso1individual(),this.isDisabled=!0)},cargarComboPaso1(){console.log("[PASO 1] Solicitando habitaciones disponibles..."),this.isLoading=!0,k.A.get(`${this.formData.baseUrl}/Valimar/GetComboHabitacionesDisponibles`).then((e=>{console.log("[PASO 1] Habitaciones disponibles recibidas:",e.data);const o=this.$refs.paso1Component;o?(o.$refs.paso1.innerHTML=e.data,o.$refs.paso1.style.display="block"):console.error("[PASO 1] Componente Paso1 no encontrado.")})).catch((e=>{console.error("[PASO 1] Error al obtener habitaciones:",e)})).finally((()=>{this.isLoading=!1}))},lanzarPaso2(){console.log("[PASO 2] Iniciando solicitud para bloquear habitación..."),this.formData.comboHabitaciones?(this.currentStep=2,this.isLoading=!0,k.A.post(`${this.formData.baseUrl}/Valimar/BloquearHabitacion`,{capacidad:this.formData.comboHabitaciones.split(",")[0],camas:this.formData.comboHabitaciones.split(",")[1]}).then((e=>{console.log("[PASO 2] Respuesta recibida:",e.data);const o=this.$refs.paso2Component;if(console.log("[PASO 2] Comprobando valores:"),console.log("paso2Component:",o),console.log("response.data:",e.data),o&&e.data&&""!==e.data.trim()){this.formData.idhabitacion=e.data;const a=`\n          <div class="alert alert-info" role="alert">\n            <h4 class="alert-heading">Habitación Bloqueada</h4>\n            <p>Habitación bloqueada temporalmente: <strong>${e.data}</strong>.</p>\n            <hr>\n            <p class="mb-0">Dispone de 10 minutos para completar la reserva.</p>\n          </div>`;o.mostrarMensaje(a)}else console.warn("[PASO 2] No se pudo bloquear habitación o datos incompletos."),this.$refs.paso2Component&&"function"===typeof this.$refs.paso2Component.mostrarError?this.$refs.paso2Component.mostrarError("No se pudo bloquear la habitación. Inténtelo de nuevo."):console.error("[PASO 2] No se pudo encontrar el método mostrarError en el componente.")})).catch((e=>{console.error("[PASO 2] Error al bloquear habitación:",e),this.$refs.paso2Component&&"function"===typeof this.$refs.paso2Component.mostrarError&&this.$refs.paso2Component.mostrarError("Error al conectar con el servicio. Inténtelo más tarde.")}))):console.error("[PASO 2] comboHabitaciones está vacío. Abortando.")},generarPaso3(e,o){if(console.log(`[PASO 3] Generando paso 3 con capacidad: ${e}, idHabitacion: ${o}`),!e||!o)return void console.error(`[PASO 3] Datos incompletos. Capacidad: ${e}, idHabitacion: ${o}. Abandonando proceso.`);if(e=parseInt(e,10),isNaN(e))return void console.error(`[PASO 3] Capacidad no es un número válido: ${e}. Abandonando proceso.`);this.currentStep=3;const a=this.$refs.paso3Component;if(a){console.log("Valor Inscritos -> "+this.inscritos.toString),this.inscritos=[],console.log("Current Step:"+this.currentStep);for(let o=0;o<e;o++)console.log(`[PASO 3] Creamos usuario: ${o}`),Vue.set(this.inscritos,o,{nombre:"",apellidos:"",pseudonimo:"",email:"",telefono:"",nif:"",aceptaCondiciones:!1,menor:o>0,conBebes:0!==o&&null,fechaBebe:0===o?"":null});this.$nextTick((()=>{console.log("La vista debería haberse actualizado ahora para el paso 3.")}))}else console.error("[PASO 3] Referencia a paso3Component no encontrada.")}}},Ae=(0,L.A)(_e,[["render",b],["__scopeId","data-v-121afeea"]]),Ie=Ae;document.addEventListener("DOMContentLoaded",(()=>{const e=document.querySelector("#app");console.log(e),e&&!e.__vue_app__?(console.log("Montando la aplicación..."),(0,n.Ef)(Ie).mount("#app")):console.log("La aplicación ya está montada.")}))}},o={};function a(n){var t=o[n];if(void 0!==t)return t.exports;var i=o[n]={exports:{}};return e[n].call(i.exports,i,i.exports,a),i.exports}a.m=e,(()=>{var e=[];a.O=(o,n,t,i)=>{if(!n){var s=1/0;for(d=0;d<e.length;d++){for(var[n,t,i]=e[d],r=!0,l=0;l<n.length;l++)(!1&i||s>=i)&&Object.keys(a.O).every((e=>a.O[e](n[l])))?n.splice(l--,1):(r=!1,i<s&&(s=i));if(r){e.splice(d--,1);var c=t();void 0!==c&&(o=c)}}return o}i=i||0;for(var d=e.length;d>0&&e[d-1][2]>i;d--)e[d]=e[d-1];e[d]=[n,t,i]}})(),(()=>{a.n=e=>{var o=e&&e.__esModule?()=>e["default"]:()=>e;return a.d(o,{a:o}),o}})(),(()=>{a.d=(e,o)=>{for(var n in o)a.o(o,n)&&!a.o(e,n)&&Object.defineProperty(e,n,{enumerable:!0,get:o[n]})}})(),(()=>{a.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()})(),(()=>{a.o=(e,o)=>Object.prototype.hasOwnProperty.call(e,o)})(),(()=>{a.r=e=>{"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}})(),(()=>{var e={524:0};a.O.j=o=>0===e[o];var o=(o,n)=>{var t,i,[s,r,l]=n,c=0;if(s.some((o=>0!==e[o]))){for(t in r)a.o(r,t)&&(a.m[t]=r[t]);if(l)var d=l(a)}for(o&&o(n);c<s.length;c++)i=s[c],a.o(e,i)&&e[i]&&e[i][0](),e[i]=0;return a.O(d)},n=self["webpackChunkValimarFrontal"]=self["webpackChunkValimarFrontal"]||[];n.forEach(o.bind(null,0)),n.push=o.bind(null,n.push.bind(n))})();var n=a.O(void 0,[504],(()=>a(953)));n=a.O(n)})();
//# sourceMappingURL=app.7a7b3db7.js.map