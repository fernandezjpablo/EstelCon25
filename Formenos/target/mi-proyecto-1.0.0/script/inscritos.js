/**
 * 
 */
 
 

 function formatearOcupantes(lista)
 {
 var resultado="<br><b><a href=\"javascript:void($('#verocupantes').toggle());\">Ver ocupantes de la habitación:</b></a><br><div id=\"verocupantes\" style=\"display:none\">";
 var listado=JSON.parse(lista);
 for (var l=0;l<listado.length;l++)
 {
 	resultado+=listado[l]+"<br>";
 }
 resultado+="</div>";
 return resultado;
 }
 
 function pintarDatos(usuario)
 {
 $("#con_bebes").show();
 $("#txt_con_bebes").show();
  $("#txt_chk_con_bebes").show();
 $("#chk_con_bebes").show();
 $("#txt_menor").show();
 $("#menor").show();
 $.each(usuario, function(key,val){
 if (key=="ocupantes")
	{
		$("#datosocupantes").html(formatearOcupantes(val));
		$("#datosocupantes").show();
	}
	else if (key=="estadoPagos")
	{
	var notificacionmsg="";
		if (val==0) notificacionmsg="Pagada"; 	
	    if (val>0) notificacionmsg="Pagada (con excedente de "+val+"€)";
	    if (val<0) notificacionmsg="Pendiente ("+val+"€)";
	    if (val==0.0) notificacionmsg="Pagada";
	    $("#notificacion").html(notificacionmsg);
	}
	else
	if (val==true || val==false)
	{
	$( "#"+key ).prop( "checked", val );
	}
	else
	{
	if ((key=="observaciones") || (key=="tipoHabitacion") || (key=="importePlaza"))
	{
	     $("#"+key).html(val);
	     if ((key=="tipoHabitacion") && (val=="Plaza individual en habitación compartida"))
	     {
	     $("#con_bebes").hide();
 		$("#txt_con_bebes").hide();
 		$("#txt_menor").hide();
 		$("#menor").hide();
	     }

	    }
	else
	{
         $("#"+key).val(val);
         if (key=="con_bebes")
         {
			if (val!="0" && val!=null)
         	{
         	$( "#chk_con_bebes").prop( "checked", true );
         	}
         	else
         	{
         	$( "#chk_con_bebes").prop( "checked", false );
         	}
		}
    }
    }
    });
if (usuario["estado"]==9) $("#notificacion").html("Baja");
 }

 function calendario()
 {
 /*
 $("#inscripciones").hide();
   $("#tituloInscripciones").show();
     $("#tituloInscripciones").html("");
 $("#misInscripciones").hide();
 $("#gestionar").hide();
 $("#propuestas").hide();
 $("#planificaciones").show();
 */
 window.open("/Formenos/MostrarCalendario");
 }
 
 function seleccionarBoton(nombre)
 {
 $("#btnDisponibles").prop("class","box_button_zi");
 $("#btnGestion").prop("class","box_button_zi");
 $("#btnInscritas").prop("class","box_button_zi");
 $("#btnPropuesta").prop("class","box_button_zi");
 $("#btn"+nombre).prop("class","box_button_zi_selected");
 }
 function inscripciones()
 {
 $("#inscripciones").show();
   $("#tituloInscripciones").html("<span  class=\"item_actividad\">Mostrar sólo actividades con plazas libres</span><input type=\"checkbox\" id=\"filtrarLibres\" onClick=\"toggleLibres();\"><hr>");
    $("#tituloInscripciones").show();
 $("#misInscripciones").hide();
 $("#gestionar").hide();
  $("#propuestas").hide();
 $("#planificaciones").hide();
 seleccionarBoton("Disponibles");
 }
 
function misInscripciones()
 {
   $("#tituloInscripciones").hide();
 $("#misInscripciones").show();
 $("#inscripciones").hide();
 $("#gestionar").hide();
  $("#propuestas").hide();
 $("#planificaciones").hide();
 seleccionarBoton("Inscritas");
 }
 
 function gestionarMisActividades()
 {
  $("#tituloInscripciones").hide();
 $("#gestionar").show();
 $("#inscripciones").hide();
 $("#misInscripciones").hide();
 $("#planificaciones").hide();
 $("#propuestas").hide();
 seleccionarBoton("Gestion");
 }
 
 function proponerActividad()
 {
   $("#tituloInscripciones").hide();
  $("#gestionar").hide();
 $("#inscripciones").hide();
 $("#misInscripciones").hide();
 $("#planificaciones").hide();
 $("#propuestas").hide();
 $('#formPropuestas')[0].reset();
 $("#responsableActividad").html($("#nombre").val()+" "+$("#apellido").val());
  $("#emailResponsableActividad").html($("#email").val());
		tinymce.init({
			selector: 'textarea#descripcionActividad',
			convert_urls:false,
			paste_data_images:false,
			menubar:false
			});
  $("#propuestas").show();
  seleccionarBoton("Propuesta");
 }
 
 function ocultarProponer()
 {
  $("#propuestas").hide();
 }
 function misDatos()
 {
  			$("#notificacionesTabla").html("");
			 $("#notificacionesTabla").html("<span style=\"color:red\">Cargando información sobre actividades</span>");
 			$.ajax({
		url:"/Formenos/ObtenerActividadesUsuario",
		type:"POST",
		data:"idusuario="+$("#id").val(),
		success:function(respuesta,estados){
			$("#notificacionesTabla").html("");
			cargarDatos(respuesta);
			}
		});
 $("#datos").show();
 $("#checkin").hide();
 $("#actividades").hide();

 }
 
 function toggleLibres()
 {
 if ($("#filtrarLibres").is(":checked"))
 {
  $('.actividad').hide();
  $('.libre').show();
 }
 else
 {
 $('.actividad').show();
 }
 }
 function cargarDatos(respuesta)
 {
 var objeto=JSON.parse(respuesta);

 if (objeto["propuestas"])
	{
		$("#btnPropuesta").show();
	}
else	
{
		$("#btnPropuesta").hide();
	}
 var inscribibles=objeto["inscribibles"];
 var contenidoInscribibles="";
 
 if (inscribibles.length==0) contenidoInscribibles="<span class=\"titulo_act\">(No hay actividades disponibles para inscripción)</span>"; 

 var linea="";
 for (var i=0;i<inscribibles.length;i++)
 {
 var estilo="actividad";
 if (inscribibles[i]["aforo"]>inscribibles[i]["ocupacion"]) estilo="actividad libre";
 var aforoStr=inscribibles[i]["aforo"];
 if (inscribibles[i]["aforo"]==0) aforoStr="Sin límite de aforo";
 linea="<span onClick=\"$('#act-"+inscribibles[i]["idActividad"]+"').toggle();\" class=\""+estilo+"\">"+inscribibles[i]["nombreActividad"]+"<br></span><span onClick=\"$('#act-"+inscribibles[i]["idActividad"]+"').toggle();\" class=\"contenido4 "+estilo+"\">(Plazas: "+aforoStr+";Reservadas: "+inscribibles[i]["ocupacion"]+";En lista de espera: "+inscribibles[i]["espera"]+")<br></span>";
 linea+="<br><div class=\"contenido4\" id=\"act-"+inscribibles[i]["idActividad"]+"\" style=\"display:none\">";
 linea+="Responsables:"+inscribibles[i]["nombres_responsables"]+"<br>";
 linea+="Descripción:<br>";
 linea+=inscribibles[i]["descripcion"]+"<br>";


 linea+="Disponibles:"+aforoStr+"<br>";
 linea+="Reservadas actualmente:"+inscribibles[i]["ocupacion"]+"<br>";
 linea+="En lista de espera:"+inscribibles[i]["espera"]+"<br>"; 
 linea+="Público:"+inscribibles[i]["publico"]+"<br>";
 if (inscribibles[i]["pagoAdicional"]) linea+="Requiere pago adicional<br>";
 if (inscribibles[i]["aforo"]>0)
 {
 if (inscribibles[i]["ocupacion"]<inscribibles[i]["aforo"])
 {
 linea+="<br><input type=\"button\" value=\"Inscribirse a esta actividad\" onClick=\"inscribir('"+inscribibles[i]["idActividad"]+"',"+inscribibles[i]["pagoAdicional"]+");\"><br>";
 }
 else
 {
 linea+="Actividad completa<br>";
 linea+="<br><input type=\"button\" value=\"Apuntarse a la lista de espera\" onClick=\"inscribirEspera('"+inscribibles[i]["idActividad"]+"',"+inscribibles[i]["pagoAdicional"]+");\"><br>";
 }
 }
 else
 {
 linea+="<br><input type=\"button\" value=\"Inscribirse a esta actividad\" onClick=\"inscribir('"+inscribibles[i]["idActividad"]+"',"+inscribibles[i]["pagoAdicional"]+");\"><br>";
 }
 linea+="</div><hr>";
 contenidoInscribibles+=linea;
 }
 $("#inscripciones").html(contenidoInscribibles);
 
 var misactividades=objeto["propias"];
 linea="";
 var contenidoPropias="<span><b><u>Mis propuestas (Gestión)</u></b></span><br>";
 if (misactividades.length==0) 
 {
 contenidoPropias="<span class=\"titulo_act\" style=\"margin-left:30%\">No tienes ninguna actividad propuesta por ti.</span><br>";
 }
 else
 {
 contenidoPropias="";
 var estadoActividadStr="";
 var estadoActividad;
 for (var i=0;i<misactividades.length;i++)
 {
 estadoActividad=misactividades[i]["estado"];
 if (estadoActividad==9) 
 {
 estadoActividadStr="Propuesta enviada";
 }
 else if (estadoActividad==0) 
 {
 estadoActividadStr="No planificada. Inscripciones desactivadas";
 }
 else if (estadoActividad==1) 
 {
 estadoActividadStr="No planificada. Inscripciones activas.";
 }
  else if (estadoActividad==4) 
 {
 estadoActividadStr="Planificada. Inscripciones desactivadas.";
 }
  else if (estadoActividad==5) 
 {
 estadoActividadStr="Planificada. Inscripciones activas.";
 }
 linea="";
 
 linea+="<span class=\"item_actividad\">"+misactividades[i]["nombreActividad"]+"</span><br>";
 linea+="<span>Estado:"+estadoActividadStr+"</span><br><input type=\"button\" onClick=\"editarActividad('"+misactividades[i]["idActividad"]+"')\" value=\"Editar Actividad\"> <input type=\"button\" onClick=\"gestionarInscritosActividad('"+misactividades[i]["idActividad"]+"','"+misactividades[i]["pagoAdicional"]+"')\" value=\"Gestión de Inscritos\">";
 linea+="<div class=\"contenido4\" >";
 linea+="<div style=\"display:none\" id=\"mngactividad-"+misactividades[i]["idActividad"]+"\">Duración estimada (en minutos):<input type=\"text\" id=\"duracion-"+misactividades[i]["idActividad"]+"\" value=\""+misactividades[i]["duracion"]+"\"><br>Observaciones internas:<br><textarea id=\"observaciones-"+misactividades[i]["idActividad"]+"\">"+misactividades[i]["observaciones"]+"</textarea><br>Requisitos:<br><textarea id=\"requisitos-"+misactividades[i]["idActividad"]+"\">"+misactividades[i]["requisitos"]+"</textarea><br><textarea id=\"descripcion-"+misactividades[i]["idActividad"]+"\">"+misactividades[i]["descripcion"]+"</textarea><br>";
 linea+="<input type=\"button\" value=\"Actualizar\" onClick=\"guardarDescripcion('"+misactividades[i]["idActividad"]+"')\";></div>";
 linea+="<div style=\"display:none\" id=\"gestioninscritosactividad-"+misactividades[i]["idActividad"]+"\">";
 linea+="<input type=\"button\" value=\"Exportar listado\" onClick=\"window.open('ObtenerInscritosActividadCsv?idactividad="+misactividades[i]["idActividad"]+"')\";>";
 linea+="<div id=\"inscritos-"+misactividades[i]["idActividad"]+"\"></div>";
 linea+="<div id=\"comunicacion-"+misactividades[i]["idActividad"]+"\" style=\"display:none\">";
 linea+="Destinatarios: <br><input type=\"checkbox\" id=\"chk-inscritos-"+misactividades[i]["idActividad"]+"\" checked> Inscritos";
 linea+="<input type=\"checkbox\" id=\"chk-espera-"+misactividades[i]["idActividad"]+"\"> En lista de espera";
 linea+=" <textarea id=\"mensajeresponsable-"+misactividades[i]["idActividad"]+"\"></textarea><br>";
 linea+="<input class=\"item_actividad\" type=\"button\" value=\"Enviar mensaje\" onClick=\"enviarMensajeInscritos('"+misactividades[i]["idActividad"]+"')\";></div></div><br>";
 linea+="</div>";
 contenidoPropias+=linea;
  }
  

  
 }
  $("#gestionar").html(contenidoPropias);
  
  
 var misactividades=objeto["inscritas"];
 linea="";
 var contenidoInscritas="";
 if (misactividades.length==0) 
 {
 contenidoInscritas+="<span class=\"titulo_act\">(No estás apuntado a nada)</span><br>";
 }
 else
 {
 for (var i=0;i<misactividades.length;i++)
 {
 linea="<span onClick=\"$('#act-"+misactividades[i]["idActividad"]+"').toggle();\" class=\"actividad\">"+misactividades[i]["nombreActividad"];
 if (misactividades[i]["estado"]==9) linea+="(En lista de espera)<br>";
 linea+="</span> <br>";
 linea+="<div class=\"contenido4\" id=\"act-"+misactividades[i]["idActividad"]+"\" style=\"display:none\">";
 linea+="Responsables:"+misactividades[i]["nombres_responsables"]+"<br>";
 
 var aforoStr="Sin aforo limitado";
 if (misactividades[i]["aforo"]>0)
 {
 aforoStr=misactividades[i]["aforo"];
 }

 linea+="Plazas totales:"+aforoStr+"<br>";
 linea+="Público objetivo:"+misactividades[i]["publico"]+"<br>";
 linea+="Plazas reservadas:"+misactividades[i]["ocupacion"]+"<br>";
 linea+="En lista de Espera:"+misactividades[i]["lista_espera"]+"<br>"; 
 if (misactividades[i]["pagoAdicional"]) linea+="Requiere pago adicional<br>";
 linea+="Descripción:<br>";
 linea+=misactividades[i]["descripcion"]+"<br>";
 


 linea+="<input type=\"hidden\" id=\"responsables-"+misactividades[i]["idActividad"]+"\" value=\""+misactividades[i]["responsables_id"]+"\">";
 linea+="<input type=\"button\" value=\"Observaciones\" onClick=\"$('#observacionesinscrito-"+misactividades[i]["idActividad"]+"').toggle();\">";
 linea+="<br>";
 var observacionesStr=misactividades[i]["observaciones"];
 if (misactividades[i]["observaciones"]===undefined)
 {
	observacionesStr="";
 }
 var estadoTexto="";
 if (misactividades[i]["estadoActividad"]==0)
 {
 	estadoTexto="disabled";
 }
 linea+="<div id='observacionesinscrito-"+misactividades[i]["idActividad"]+"' style='display:none'>";
 linea+="<textarea "+estadoTexto+" id=\"bodyobservaciones-"+misactividades[i]["idActividad"]+"\">"+observacionesStr+"</textarea>";
 if (misactividades[i]["estadoActividad"]!=0)
 {
 linea+="<br><input type=\"button\" value=\"Guardar Observaciones\" onClick=\"guardarObservaciones('"+misactividades[i]["idActividad"]+"');\"></div>";
 }
 linea+="<input type=\"button\" value=\"Enviar mensaje\" onClick=\"msgResponsables('"+misactividades[i]["idActividad"]+"');\">";
 linea+="<br>";
 linea+="<div id='mensajeinscrito-"+misactividades[i]["idActividad"]+"' style='display:none'>";
 linea+="<textarea id=\"bodymsg-"+misactividades[i]["idActividad"]+"\"></textarea><input type=\"button\" value=\"Enviar\" onClick=\"sendMensajeResponsables('"+misactividades[i]["idActividad"]+"');\"></div>";
 if (misactividades[i]["estadoActividad"]!=0)
 {
 linea+="<input type=\"button\" onClick=\"bajaInscripcion('"+misactividades[i]["idActividad"]+"','"+$("#id").val()+"');\" value=\"Darse de baja\">";
 }
  linea+="</div></div><br>";
 contenidoInscritas+=linea;
 }
  
 }
 $("#misInscripciones").html(contenidoInscritas);
 var flotantes=objeto["flotantes"];
 var contenidoFlotante="";
 if (flotantes.length>0) 
 {
 $("#actividadesflotantes").show();
 }
 else
 {
 $("#actividadesflotantes").hide();
 }
  for (var f=0;f<flotantes.length;f++)
 {
 	linea=flotantes[f]["nombreActividad"]+" <input type=\"button\" value=\"Más info\" onClick=\"masInfo('"+flotantes[f]["idActividad"]+"');\">";
 	linea+="<div style=\"display:none\" id=\"descripcion-"+flotantes[f]["idActividad"]+"\">"+flotantes[f]["descripcion"]+"</div>";
 	linea+="<input type=\"hidden\" id=\"pago-"+flotantes[f]["idActividad"]+"\" value=\""+flotantes[f]["pagoAdicional"]+"\">";
 	linea+="<br>";
 	contenidoFlotante+=linea;
 }
 $("#actividadesflotantes").html(contenidoFlotante);
 }
 
 function msgResponsables(idactividad)
 {
 $("#mensajeinscrito-"+idactividad).toggle();
 if ($("#mensajeinscrito-"+idactividad).is(":visible"))
 {
  				tinymce.init({
					selector: 'textarea#bodymsg-'+idactividad,
		  			convert_urls:false,					
					paste_data_images:false,
					menubar:false
					});
 }
 }
 
 function masInfo(idactividad)
 {
 contenido=$("#descripcion-"+idactividad).html();
 contenido+="<br>";
 if ($("#pago-"+idactividad).val()=="true")
 {
 contenido+="(Requiere pago adicional)";
 }
 
 contenido+="<br><br><input class=\"box_button_zi\" type=\"button\" value=\"Apúntame\" onClick=\"inscribir('"+idactividad+"',"+$("#pago-"+idactividad).val()+");\">";
 contenido+="&nbsp;<input class=\"box_button_zi\" type=\"button\" onClick=\"volverMasInfo();\" value=\"Volver\">";
 $("#descripcionActividad").html(contenido);
 
 
$("#datos").hide();
$("#checkin").hide();
$("#infoActividad").show(); 
 }
 
 function volverMasInfo()
 {
 $("#descripcionActividad").html("");
$("#datos").show();
$("#checkin").hide();
$("#infoActividad").hide(); 
 }

function enviarMensajeInscritos(idactividad)
{
var tipoEnvio="";
var enEspera=$("#chk-espera-"+idactividad).is(':checked');
var enInscritos=$("#chk-inscritos-"+idactividad).is(':checked');
if (enEspera && enInscritos)
{
	tipoEnvio="ambos";
}
else
{
if (enEspera) {
	tipoEnvio="espera";
	}
	else
	{
		tipoEnvio="inscritos";
	}

}
$.ajax({
		url:"/Formenos/GenerarMensajeSaliente",
		type:"POST",
		data:"tipoEnvio="+tipoEnvio+"&idactividad="+idactividad+"&from="+$("#id").val()+"&data="+encodeURIComponent(tinymce.get("mensajeresponsable-"+idactividad).getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Mensaje enviado");
				$("#mensajeresponsable-"+idactividad).html("");
				tinymce.activeEditor.setContent("");
				$("#comunicacion-"+idactividad).hide();
				}
			else
				{
				alert("Error al generar el mensaje: "+respObj["mensaje"]);
				}
		}
});
}
 
 
function sendMensajeResponsables(idactividad)
{
$.ajax({
		url:"/Formenos/GenerarMensajeSaliente",
		type:"POST",
		data:"tipoEnvio=responsables&idactividad="+idactividad+"&from="+$("#id").val()+"&data="+encodeURIComponent(tinymce.get("bodymsg-"+idactividad).getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Mensaje enviado");
				$("#bodymsg-"+idactividad).html("");
				tinymce.activeEditor.setContent("");
				$("#mensajeinscrito-"+idactividad).hide();
				}
			else
				{
				alert("Error al generar el mensaje: "+respObj["mensaje"]);
				}
		}
});
}
 
 function cargarDatosInscritos(idactividad,pago,inscritos)
 {
 var contenido="";
 var inscritos=JSON.parse(inscritos);
 if (inscritos.length>0)
 {
 contenido="<table border=1px><thead><th><b><u>Inscritos en la actividad:</u></b></th></thead>";
 contenido+="<tbody>";
 for (var i=0;i<inscritos.length;i++)
 {
 var estiloinscrito="";
   if (inscritos[i]["estado"]=="9")
 	{
 	estiloinscrito="style=\"background-color:darkgray\"";
 	}

 
 contenido+="<tr><td "+estiloinscrito+">";
 contenido+=inscritos[i]["nombre"]+" "+inscritos[i]["apellidos"];
 if (inscritos[i]["pseudonimo"]!="")
 {
 contenido+="-\""+inscritos[i]["pseudonimo"]+"\"";
 }
 contenido+=" Fecha de Inscripción:"+new Date(inscritos[i]["fecha"]).toLocaleString();
  if (inscritos[i]["estado"]=="9")
 	{
 	contenido+="<b>(Lista de Espera)</b>";
 	}
 if (inscritos[i]["menor"])
 {
 contenido+=" (Menor de edad)";
 }
 
 
 var strTelegram=inscritos[i]["telegram"];
 if (typeof strTelegram === "undefined") strTelegram="No especificado";
 contenido+="<br>email: "+inscritos[i]["email"]+"; teléfono: "+inscritos[i]["telefono"]+"; Telegram: "+strTelegram+"<br><br>";
 if (pago=='true'){
 if (inscritos[i]["estado"]=="1")
 	{
 	contenido+="(Pago realizado)";
 	}
 else
   	{
 	contenido+="(Pendiente de pago)";
 	}
 	}
 	var observacionesStr=inscritos[i]["observaciones"];
 	if (observacionesStr===undefined) observacionesStr="(No hay observaciones)";
 	 contenido+="<br><b>Observaciones del inscrito:</b><br>"+observacionesStr+"<br><br>";
 contenido+="</td></tr>";
 }
 contenido+="</table>";
 contenido+="<input type=\"button\" value=\"Redactar Mensaje\" onClick=\"$('#comunicacion-"+idactividad+"').toggle();\">";
 }
 else
 {
 contenido="<span>Aún no hay inscritos en esta actividad</span>";
 }
 
 $("#inscritos-"+idactividad).html(contenido);
 }
 function cargarInscritosActividad(idactividad,pago)
 {
  	$.ajax({
		url:"/Formenos/ObtenerInscritosActividad",
		type:"POST",
		data:"idactividad="+idactividad,
		success:function(respuesta,estados){
			cargarDatosInscritos(idactividad,pago,respuesta);
			
			
			}
		});
 }
 
 function gestionarInscritosActividad(idactividad,pago)
 {
 $('#gestioninscritosactividad-'+idactividad).toggle();
  if ($('#gestioninscritosactividad-'+idactividad).is(":visible"))
 {
 cargarInscritosActividad(idactividad,pago);
 				tinymce.init({
					selector: 'textarea#mensajeresponsable-'+idactividad,
		  			convert_urls:false,					
					paste_data_images:true,
					menubar:false
					});
 }
 }
 
 function editarActividad(idactividad)
 {
 $('#mngactividad-'+idactividad).toggle();
  if ($('#mngactividad-'+idactividad).is(":visible"))
 {
 				tinymce.init({
					selector: 'textarea#descripcion-'+idactividad,
					convert_urls:false,		
					paste_data_images:true,
					menubar:false
					});
 }
 }
 
 
  function solicitarBaja()
 {
 var usuario=JSON.parse("{}");
 usuario["nombre"]=$("#nombre").val();
 usuario["apellido"]=$("#apellido").val();
 usuario["telefono"]=$("#telefono").val();
 usuario["email"]=$("#email").val();
 usuario["telegram"]=$("#telegram").val();
 usuario["nif"]=$("#nif").val();
 usuario["pseudonimo"]=$("#pseudonimo").val();
 usuario["smial"]=$("#smial").val();
 usuario["alergias_txt"]=$("#alergias_txt").val();
 usuario["alimentos_txt"]=$("#alimentos_txt").val();
 usuario["observaciones"]=encodeURIComponent(tinymce.get('observaciones').getContent());
 usuario["alergias"]=$("#alergias").is(':checked');
 usuario["alimentos"]=$("#alimentos").is(':checked');
 usuario["menor"]=$("#menor").is(':checked');
 usuario["id"]=$("#id").val();
 var confirmacion=confirm("Estás a punto de solicitar la baja, este proceso no se puede deshacer");
 if (confirmacion)
 {
 confirmacion=confirm("¿Estás completamente segur@?");
 if (confirmacion)
 {
 $.ajax({
		url:"/Formenos/SolicitarBaja",
		type:"POST",
		data:usuario,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Solicitud de baja remitida.");
				}
			else
				{
				alert("Se ha producido un error al enviar la petición, contacta con la organización, por favor.");
				}
		}
});
}
}
 }
 
 
 function guardarDescripcion(idactividad)
{
$.ajax({
		url:"/Formenos/GuardarDescripcion",
		type:"POST",
		data:"actividad="+idactividad+"&duracion="+$("#duracion-"+idactividad).val()+"&observaciones="+encodeURIComponent($("#observaciones-"+idactividad).val()).replace(/\+/g,"%2B")+"&requisitos="+encodeURIComponent($("#requisitos-"+idactividad).val()).replace(/\+/g,"%2B")+"&descripcion="+encodeURIComponent(tinymce.get('descripcion-'+idactividad).getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Actividad actualizada");
				}
			else
				{
				alert("Error al actualizar la actividad: "+respObj["resultado"]);
				}
		}
});
}
 function cargarMisActividades()
 {
 $.ajax({
		url:"/Formenos/ObtenerActividadesUsuario",
		type:"POST",
		data:"idusuario="+$("#id").val(),
		success:function(respuesta,estados){
			$("#notificacionesTabla").html("");
			cargarDatos(respuesta);
			}
		});
 }
 
 function toggleDia(dia)
 {
 $(".planificaciondia").hide();
 $('#'+dia).show();
 }
 
 function misActividades()
 {
 $("#datos").hide();
 $("#checkin").hide();
 $("#actividades").show();
 $("#infoActividad").hide(); 
 //$("#notificacionesTabla").html("<span style=\"color:red\">Cargando calendario de actividades</span>");
  $.ajax({
		url:"/Formenos/ObtenerActividadesUsuario",
		type:"POST",
		data:"idusuario="+$("#id").val(),
		success:function(respuesta,estados){
			$("#notificacionesTabla").html("");
			cargarDatos(respuesta);
			}
		});
		/*
 	$.ajax({
		url:"/Formenos/ObtenerTablaPlanificacion",
		type:"GET",
		data:"idusuario="+$("#id").val(),
		success:function(respuesta,estados){
			$("#calendario").html(respuesta);
			$("#notificacionesTabla").html("");
			 $("#notificacionesTabla").html("<span style=\"color:red\">Cargando información sobre actividades</span>");

			}
		});
		*/
		misInscripciones();
		seleccionarBoton("Inscritas");
 }
  function recuperar()
 {

 	$.ajax({
		url:"/Formenos/RecuperarDatosAcceso",
		type:"POST",
		data:"email="+$("#emailolvido").val(),
		success:function(resp,estados){
			 alert(resp["mensaje"]);

			}
		});
 }
 

 function inscribir(idactividad,pago)
 {
  	$.ajax({
		url:"/Formenos/CheckInscripcionesAbiertas",
		data:"idusuario="+$("#id").val()+"&idactividad="+idactividad+"&pago="+pago,
		type:"GET",
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"])
				{
				var resp;
				var mensaje="";
				if (respObj["propias"]!="") mensaje+="Esta actividad coincide con actividades tuyas: "+respObj["propias"]+"\n";
				if (respObj["inscritas"]!="") mensaje+="Esta actividad coincide con actividades inscritas: "+respObj["inscritas"]+"\n";
				mensaje+="Al apuntarte das tu consentimiento a que la persona que organiza la actividad tenga acceso a tus datos de contacto para coordinar la misma"+"\n";
				mensaje+="¿Confirmas que te apuntas?";
				resp=confirm(mensaje);
				if (resp)
 				{
 					$.ajax({
						url:"/Formenos/InscribirActividad",
						type:"POST",
						data:"idusuario="+$("#id").val()+"&idactividad="+idactividad+"&pago="+pago,
						success:function(respuesta,estados){
							var respObj=JSON.parse(respuesta);
							if (respObj["respuesta"])
							{
								alert("Apuntad@");
								misActividades();
							}
							else
							{
								alert("No se pudo hacer la inscripción");
							}
			}
		});
	}	
				}
			else
				{
				alert("Las inscripciones a actividades aún no están disponibles.");
				}
			 
			}
		});
 
 	
 }  

 function inscribirEspera(idactividad,pago)
 {
var mensaje ="Al apuntarte das tu consentimiento a que la persona que organiza la actividad tenga acceso a tus datos de contacto para coordinar la misma"+"\n";
mensaje+="¿Confirmas que te apuntas a la lista de espera?";
 var resp=confirm(mensaje);
 if (resp)
 {
 	$.ajax({
		url:"/Formenos/InscribirEsperaActividad",
		type:"POST",
		data:"idusuario="+$("#id").val()+"&idactividad="+idactividad+"&pago="+pago,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Apuntad@ a la lista de espera");
				misActividades();
				}
			else
				{
				alert("No se pudo hacer la inscripción");
				}
			 
			}
		});
	}		
 }   
 
  function bajaInscripcion(idactividad,idusuario)
 {
 var resp=confirm("¿Seguro que quieres darte de baja de esta actividad?");
 if (resp)
 {
 	$.ajax({
		url:"/Formenos/BajaActividad",
		type:"POST",
		data:"idusuario="+idusuario+"&idactividad="+idactividad,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Baja realizada");
				misActividades();
				}
			else
				{
				alert("No se pudo eliminar la inscripción en la actividad");
				}
			 
			}
		});
	}		
 }  
 
  function changePassword()
 {
 var oldpass=prompt("Introduzca la actual contraseña");
 var pass=prompt("Introduzca la nueva contraseña");
 $.ajax({
		url:"/Formenos/UpdatePassword",
		type:"POST",
		data:"oldpass="+oldpass+"&newpass="+pass,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Contraseña actualizada");
				}
			else
				{
				alert("No se pudo actualizar la contraseña");
				}
		}
});
 }
 
 
 
 function actualizarDatos()
 {
 var usuario=JSON.parse("{}");
 usuario["nombre"]=$("#nombre").val();
 usuario["apellido"]=$("#apellido").val();
 usuario["telefono"]=$("#telefono").val();
 usuario["email"]=$("#email").val();
 usuario["telegram"]=$("#telegram").val();
 usuario["nif"]=$("#nif").val();
 usuario["pseudonimo"]=$("#pseudonimo").val();
 usuario["smial"]=$("#smial").val();
 usuario["alergias_txt"]=$("#alergias_txt").val();
 usuario["alimentos_txt"]=$("#alimentos_txt").val();
 usuario["observaciones"]=encodeURIComponent(tinymce.get('observaciones').getContent());
 usuario["alergias"]=$("#alergias").is(':checked');
 usuario["alimentos"]=$("#alimentos").is(':checked');
 usuario["menor"]=$("#menor").is(':checked');
 usuario["con_bebes"]=$("#con_bebes").val();
 usuario["id"]=$("#id").val();
 
 $.ajax({
		url:"/Formenos/ActualizarPerfil",
		type:"POST",
		data:usuario,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Datos actualizados");
				cargarPerfil();
				}
			else
				{
				alert("No se pudieron actualizar los datos: "+respObj["respuesta"]);
				}
		}
});
 }
 
 
  function guardarObservaciones(idactividad)
 {

 $.ajax({
		url:"/Formenos/ActualizarObservacionesInscripcionActividad",
		type:"POST",
		data:"idusuario="+$("#id").val()+"&idactividad="+idactividad+"&observaciones="+encodeURIComponent($("#bodyobservaciones-"+idactividad).val()),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Observaciones actualizadas");
				}
			else
				{
				alert("No se pudieron actualizar las observaciones: "+respObj["respuesta"]);
				}
		}
});
 }
 
   function guardarCheckin()
 {

 $.ajax({
		url:"/Formenos/GuardarCheckin",
		type:"POST",
		data:"ciudad="+$("#ciudadCheckin").val()+"&pais="+$("#paisCheckin").val()+"&codigo_postal="+$("#cpCheckin").val()+"&direccion="+encodeURIComponent($("#direccionCheckin").val())+"&nacimiento="+$("#nacimientoCheckin").val()+"&expedicion="+$("#expedicionCheckin").val(),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Check-in registrado");
				}
			else
				{
				alert("No se pudo registrar el check-in: "+respObj["respuesta"]);
				}
		}
});
 }
function checkinHotel()
 {
 $("#nombreCheckin").val($("#nombre").val());
  $("#apellidoCheckin").val($("#apellido").val());
   $("#nifCheckin").val($("#nif").val());
 	$("#checkin").show();
 	 $("#datos").hide();
 	$("#actividades").hide();
 }
 
 
 function QRCheckin()
 {
	window.open("/Formenos/checkin.html");
 }
 
 function cargarCheckin()
 {
 $.ajax({
		url:"/Formenos/GenerarCheckin",
		type:"GET",
		success:function(respuesta,estados){
				$("#datosiden").html(respuesta);
		}
});
 }
 
 function mostrarEsteltienda()
 {
 window.open("esteltienda.html");
 }
 function cargarPerfil()
 {
 $.ajax({
		url:"/Formenos/GetPerfil",
		type:"POST",
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				pintarDatos(respObj["usuario"]);
				if (respObj["checkin"])
				{
					$("#btnCheckin").show();
					$("#btnQR").show();
					$("#nacimientoCheckin").val(respObj["datosCheckin"]["fechaNacimiento"]);
					$("#direccionCheckin").val(respObj["datosCheckin"]["direccion"]);
					$("#paisCheckin").val(respObj["datosCheckin"]["pais"]);
					$("#ciudadCheckin").val(respObj["datosCheckin"]["ciudad"]);
					$("#cpCheckin").val(respObj["datosCheckin"]["codigo_postal"]);
					if (respObj["usuario"]["menor"])
					{
					$("#expedicionCheckin").val("00/00/0000");
					}
					else
					{
					$("#expedicionCheckin").val(respObj["datosCheckin"]["fechaExpedicion"]);
					}
					
				}
				if (respObj["esteltienda"])
				{
					$("#btnEsteltienda").show();
				}
				$("#datos").show();
				$("#actividades").hide();
				$("#checkin").hide();
				tinymce.init({
					selector: 'textarea#observaciones',
				    convert_urls:false,					
					paste_data_images:false,
					menubar:false
					});
					misDatos();
				}
			else
				{

				location.href="login.html";
				}
		},
		failure:function(respuesta,estados){

				location.href="/Formenos/login.html";
		},
		error:function(respuesta,estados){

				location.href="/Formenos/login.html";
		}
});
 }
 
  function iniciarSesion()
 {
  $.ajax({
		url:"/Formenos/Login",
		type:"POST",
		data:"usuario="+$("#usuario").val()+"&password="+$("#password").val(),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="sesiongenerada")
				{
				location.href="/Formenos/portal.html";
				}
			else if (respObj["respuesta"]=="loginerroneo")
				{
				$("#notificacion").html("Usuario o contraseña no válidos");
				}
				else
				{
				$("#notificacion").html("No se pudo iniciar sesión.");
				}
		},
		failure:function(respuesta,estados){

				$("#notificacion").html("No se pudo iniciar sesión.");
		},
		error:function(respuesta,estados){

				$("#notificacion").html("No se pudo iniciar sesión.");
		}
});
 
 }
 
 function cerrarSesion()
 {
  $.ajax({
		url:"/Formenos/CloseSession",
		type:"POST",
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				location.href="/Formenos/login.html";
				}
			else
				{
				location.href="/Formenos/login.html";
				}
		},
		failure:function(respuesta,estados){

				location.href="/Formenos/login.html";
		},
		error:function(respuesta,estados){

				location.href="/Formenos/login.html";
		}
});
 
 }
 
 
function enviarPropuesta()
{
var responsable=$().val();
var alertstr="";
if ($('#nombreActividad').val()=="") alertstr+="Debe indicar un nombre para la actividad.\n";
if ($('#aforoActividad').val()=="") alertstr+="Debe indicar un aforo para la actividad (si no hay límite pon 0).\n";
if ($('#duracionActividad').val()=="") alertstr+="Debe indicar una duración estimada para la actividad (si no la tiene pon 0).\n";
if ($('#publicoActividad option:selected').val()=="") alertstr+="Seleccione el público objetivo de la actividad.\n";
if (alertstr=="")
{
$.ajax({
		url:"/Formenos/NuevaActividad",
		type:"POST",
		data:"nombre="+$('#nombreActividad').val()+"&responsables="+$('#id').val()+"&duracion="+$('#duracionActividad').val()+"&aforo="+$('#aforoActividad').val()+"&pseudonimos="+$('#pseudonimoActividad').val()+"&publico="+$('#publicoActividad option:selected').val()+"&pago="+$('#pagoAdicional').is(':checked')+"&observaciones="+encodeURIComponent($('#observacionesActividad').val()).replace(/\+/g,"%2B")+"&requisitos="+encodeURIComponent($('#requisitosActividad').val()).replace(/\+/g,"%2B")+"&descripcion="+encodeURIComponent(tinymce.activeEditor.getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
				if (respObj["resultado"]=="ok")
					{
					alert("Propuesta de actividad enviada");
					$("#formPropuestas")[0].reset();
					$('#descripcionActividad').val("");	
					cargarMisActividades();
					  $("#propuestas").hide();
					}

		},
		error:function(respuesta,estados){
					alert("Error al enviar la propuesta de actividad, revise los campos.");
					cargarMisActividades();
		},
		failure:function(respuesta,estados)
		{
					alert("Error al enviar la propuesta de actividad, revise los campos.");
					cargarMisActividades();			
		}
});
}
else
{
	alert(alertstr);
}
	
}