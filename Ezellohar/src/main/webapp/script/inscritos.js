
function ejecutarTarea()
{
var tarea=$('#tarea option:selected').val();
if (tarea=="eliminar")
{
//Dar de baja, anular pagos
estadoInscripcionSeleccionadas("eliminar");
}
else if (tarea=="pendiente")
{
//anular pagos y estado en pendiente sin dar de baja
estadoInscripcionSeleccionadas("pendiente");
}
else if (tarea=="apartar")
{
estadoInscripcionSeleccionadas("apartar");
//sacar de la habitación y dejarlo apartado pero el estado de pago se mantiene
}
else if (tarea=="recuperar")
{
estadoInscripcionSeleccionadas("recuperar");
//Recuperar registro de baja (los pagos se habrán anulado en la baja anterior)
}
else if (tarea=="exportar")
{
window.open("/Ezellohar/ExportarInscritos");
}
else if (tarea=="exportarWeb")
{
window.open("/Ezellohar/ExportarInscritosWeb");
}
else if (tarea=="exportarCheckin")
{
window.open("/Ezellohar/ExportarInscritosCheckin");
}
else if (tarea=="recordar")
{
//Enviar correo recordatorio de datos de acceso
recordatorioDatosAcceso();
}
else if (tarea=="inscritos")
{
//Enviar correo recordatorio de datos de acceso
recordatorioDatosInscripcion();
}
else if (tarea=="notificarCheckin")
{
//Enviar QR o aviso de datos incorrectos/incompletos
notificarEstadoCheckin();
}
}


function recordatorioDatosInscripcion()
{


var inscritos=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	inscritos.push(elemento.value);
});
if (inscritos.length>0)
{
var resp=confirm("¿Seguro que quieres reenviar el correo de inscripción a l@s inscritos seleccionad@s?");
if (resp)
{
$.ajax({
		url:"/Ezellohar/ReenviarCorreoInscripcion",
		type:"POST",
		data:"inscritos="+inscritos,
		success:function(respuesta,estados){
		var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Correos generados");
				cargarListado("");
				}
			else
				{
				alert("Error al generar los mensajes: "+respObj["resultado"]);
				}
		}
});

}
else
{
	alert("De acuerdo, proceso cancelado.");
}
}
}

function recordatorioDatosAcceso()
{


var inscritos=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	inscritos.push(elemento.value);
});
if (inscritos.length>0)
{
var resp=confirm("¿Seguro que quieres reenviar los datos de acceso a l@s inscritos seleccionad@s?");
if (resp)
{
$.ajax({
		url:"/Ezellohar/ReenviarDatosInscripcion",
		type:"POST",
		data:"inscritos="+inscritos,
		success:function(respuesta,estados){
		var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Correos generados");
				cargarListado("");
				}
			else
				{
				alert("Error al generar los mensajes: "+respObj["resultado"]);
				}
		}
});

}
else
{
	alert("De acuerdo, proceso cancelado.");
}
}
}


function notificarEstadoCheckin()
{


var inscritos=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	inscritos.push(elemento.value);
});
if (inscritos.length>0)
{
var resp=confirm("¿Seguro que quieres notificar el estado del Checkin a las personas seleccionadas?");
if (resp)
{
$.ajax({
		url:"/Ezellohar/NotificarEstadoCheckin",
		type:"POST",
		data:"inscritos="+inscritos,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Notificaciones generadas");
				}
			else
				{
				alert("Error al generar las notificaciones: "+respObj["resultado"]);
				}
		}
});

}
else
{
	alert("De acuerdo, proceso cancelado.");
}
}
}


function estadoInscripcionSeleccionadas(estado)
{


var inscritos=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	inscritos.push(elemento.value);
});
if (inscritos.length>0)
{
var resp=confirm("Seguro que quieres modificar el estado de las inscritos seleccionadas?");
if (resp)
{
$.ajax({
		url:"/Ezellohar/EstadoInscritos",
		type:"POST",
		data:"inscritos="+inscritos+"&estado="+estado,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Estados actualizados");
				cargarListado("");
				}
			else
				{
				alert("Error al actualizar los estados: "+respObj["resultado"]);
				}
		}
});

}
else
{
	alert("De acuerdo, proceso cancelado.");
}
}
}

function cargarEstado()
{
$.ajax({
		url:"/Ezellohar/CheckStatusPlazas",
		type:"GET",
		success:function(respuesta,estados){
				$('#info').html(respuesta);

		}
});
	
}

function cargarListado()
{
	cargarListado("");
}

function toggleAll()
{
var resultado=$("#selectAll").is(':checked');
$(".seleccion").each(function(){
$(this).prop("checked",resultado);
});
}

function toggleAcceso(id)
{
if ($("#acceso-"+id).is(":visible"))
{
	$("#acceso-"+id).hide();
	$("#usuario-"+id).val('');
	$("#password-"+id).val('');
}
else
{
$.ajax({
		url:"/Ezellohar/GetAcceso",
		data:"idinscrito="+id,
		type:"POST",
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
				if (respObj["resultado"]=="ok")
				{
					$("#acceso-"+id).show();
					$("#usuario-"+id).val(respObj["usuario"]);
					$("#password-"+id).val(respObj["password"]);
				}
				else
				{
				alert("No se pudieron recuperar los datos de acceso");
				}
		}
});
}
}

function actualizarAcceso(id)
{

$.ajax({
		url:"/Ezellohar/UpdateAcceso",
		data:"idinscrito="+id+"&usuario="+$("#usuario-"+id).val()+"&password="+$("#password-"+id).val(),
		type:"POST",
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
				if (respObj["resultado"]=="ok")
				{
				alert("Datos de acceso actualizados");	
				}
				else
				{
				alert("No se pudieron actualizar los datos de acceso");
				}
		}
});

}


function togglePagosInscrito(idinscrito)
{
var contenido="";
$("#tdpagosrealizados-"+idinscrito).html("(No constan movimientos)");
if (!$("#pagosrealizados-"+idinscrito).is(":visible"))
{
$.ajax({
		url:"/Ezellohar/admin/pagos/ListPagosUsuario",
		data:"idinscrito="+idinscrito,
		type:"GET",
		success:function(respuesta,estados){
		var pagos=JSON.parse(respuesta);
		if (pagos.length>0) contenido="Movimientos registrados del inscrito:<br>";
		var estado="";
		for (var p=0;p<pagos.length;p++)
		{
		estado="";
		if (pagos[p]["estado"]==99)
		{
		estado="Anulado";
		}
		else if (pagos[p]["estado"]==9)
		{
		estado ="Devuelto";
		}
		else if (pagos[p]["estado"]==8)
		{
		estado ="Anulado";
		}
		else
		{
		estado ="Confirmado";
		}
			contenido+="<b>"+pagos[p]["fecha"]+"</b> "+pagos[p]["observaciones"]+";Importe:"+pagos[p]["importe"]+"€ ("+estado+")<br>";
		}

		if (contenido!="") $("#tdpagosrealizados-"+idinscrito).html(contenido);
		}});
}
		$("#pagosrealizados-"+idinscrito).toggle();		
}

function registrarCheckin(id)
 {

 $.ajax({
		url:"/Ezellohar/GuardarCheckin",
		type:"POST",
		data:"idinscrito="+id+"&expedicion="+$("#fechaexpedicion-"+id).val()+"&nacimiento="+$("#fechanacimiento-"+id).val(),
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

function cargarListado(opciones)
{

$.ajax({
		url:"/Ezellohar/ListInscritos",
		type:"GET",
		success:function(respuesta,estados){
			var objeto=JSON.parse(respuesta);
			var listado=JSON.parse(objeto["alojados"]);
			var listadoOut=JSON.parse(objeto["desalojados"]);
			var listadoEspera=JSON.parse(objeto["espera"]);
			var listadoCheckin=JSON.parse(objeto["checkin"]);
			var linea="";
			var contenido="";
			var estadoStr="";
			var habitacionStr="";
			var menorStr="";
			var checkinStr="";
			contenido="";
			if (listado.length>0 || listadoOut.length>0 || listadoEspera.length>0)
			{
			if (listado.length>0)
			{
			contenido+="<table class=\"tablaDatos\" style=\"width:100%\">";
			contenido+="<thead><tr><th><input id=\"selectAll\" type=\"checkbox\" onClick=\"toggleAll();\"></th><th>Num.</th><th>Nombre</th><th>NIF</th><th>Smial</th><th>Estado</th><th>Habitación</th><th>Tipo</th><th>Menor</th><th>Precio</th><th>Fecha Inscripción</th><th></th></tr></thead>";
				for (var i=0;i<listado.length;i++)
					{
					menorStr="";
					var id=listado[i]["id"];
					checkinStr="<td><a href=\"javascript:$('#infoCheckin-"+id+"').toggle();\">Sin Check-in</a></td>";;
					
					var nombre=listado[i]["nombre"];
					var apellidos=listado[i]["apellidos"];
					var email=listado[i]["email"];
					var nif=listado[i]["nif"];
					var smial=listado[i]["smial"];
					var menor=listado[i]["menor"];
					var estado=listado[i]["estado"];
					var grupal=listado[i]["grupal"];
					var pseudonimo=listado[i]["pseudonimo"];
					var fecha=new Date(listado[i]["fecha"]).toLocaleString();
					var pagoindividual="<input type=\"hidden\" id=\"grupal-"+id+"\" value="+grupal+">";
					var tipoHabitacion=listado[i]["tipoHabitacion"];
					var habitacionObj=listado[i]["habitacionObj"];
					var nombreHab=habitacionObj["identificador"];
					var importePlaza=listado[i]["importePlaza"];
					var estadoPagos=listado[i]["estadoPagos"];
					if (typeof smial==='undefined') smial="(No especificado)";
					if (smial=="") smial="(No especificado)";
					if (pseudonimo!="") pseudonimo="("+pseudonimo+")";
					if (nombreHab=="") nombreHab=habitacionObj["id"];
					if (menor) menorStr="(Menor)";
					
					if (listadoCheckin.hasOwnProperty(id))
					{
					checkinStr="<td><a href=\"javascript:$('#infoCheckin-"+id+"').toggle();\">Con Check-in</a></td>";
					}
					if (estadoPagos<0)
					{
					//estadoStr="Pendiente de pago ("+estadoPagos+") <a href=\"javascript:$('#pago-"+id+"').toggle();\">(+)</a>";
					estadoStr="Pendiente de pago ("+estadoPagos+") ";
					}
					else if (estadoPagos==0)
					{
					estadoStr="Pago completado <a href=\"javascript:$('#pago-"+id+"').toggle();\">(+)</a>";
					}
					else if (estadoPagos>=0)
					{
					estadoStr="Excedente en pagos (+"+estadoPagos+") <a href=\"javascript:$('#pago-"+id+"').toggle();\">(+)</a>";
					}
					else if (estado==9)
					{
					estadoStr="Baja";
					}
					linea="<tr><td><input class='seleccion' type='checkbox' id='chk-"+id+"' value='"+id+"'></td>";
					linea+="<td>"+(i+1)+"</td><td>"+nombre+" "+apellidos+pseudonimo+"</td><td>"+nif+"</td><td>"+smial+"</td><td>"+estadoStr+"</td><td>"+nombreHab+"</td><td>"+tipoHabitacion+"</td><td>"+menorStr+"</td><td>"+importePlaza+"</td><td>"+fecha+"</td><td><a href=\"javascript:togglePagosInscrito('"+id+"');\">Pagos</a></td>"+checkinStr+"<td><a href=\"javascript:toggleAcceso('"+id+"');\"><img style=\"width:15px;\" src=\"../../img/candado.png\"></a></td></tr>";
					linea+="<tr id=\"acceso-"+id+"\" style=\"display:none\"><td colspan=7><b>Usuario</b>: <input type=\"text\" id=\"usuario-"+id+"\">Contraseña:<input type=\"text\" id=\"password-"+id+"\"> <input onClick=\"actualizarAcceso('"+id+"');\" type=\"button\" value=\"Actualizar\"></td></tr>";
					linea+="<tr id=\"pagosrealizados-"+id+"\" style=\"display:none\"><td id=\"tdpagosrealizados-"+id+"\"  colspan=7></td></tr>";
					
					//linea+="<tr id=\"pago-"+id+"\" style=\"display:none\"><td colspan=7><b>Registrar Pago/Devolución</b>: "+pagoindividual+"Importe:<input type=\"text\" id=\"importe-"+id+"\"> <input onClick=\"registrarPago('"+id+"');\" type=\"button\" value=\"Registrar\"></td></tr>";
					
					if (listadoCheckin.hasOwnProperty(id))
					{
					linea+="<tr id=\"infoCheckin-"+id+"\" style=\"display:none\"><td colspan=7><b>Fecha Expedición NIF/Pasaporte</b>: <input type=\"date\" id=\"fechaexpedicion-"+id+"\" value=\""+listadoCheckin[id]["fechaExpedicion"]+"\"> <b>Fecha de Nacimiento:</b><input type=\"date\" id=\"fechanacimiento-"+id+"\" value=\""+listadoCheckin[id]["fechaNacimiento"]+"\"> <input onClick=\"registrarCheckin('"+id+"');\" type=\"button\" value=\"Actualizar Check-in\"> <input onClick=\"QRCheckin('"+id+"');\" type=\"button\" value=\"Ver tarjeta Check-in\"></td></tr>";
					}
					else
					{
					linea+="<tr id=\"infoCheckin-"+id+"\" style=\"display:none\"><td colspan=7><b>Fecha Expedición NIF/Pasaporte</b>: <input type=\"date\" id=\"fechaexpedicion-"+id+"\"> <b>Fecha de Nacimiento:</b><input type=\"date\" id=\"fechanacimiento-"+id+"\"> <input onClick=\"registrarCheckin('"+id+"');\" type=\"button\" value=\"Registrar Check-in\"></td></tr>";
					}
					
					
					contenido+=linea;
					}
					contenido+="</table>";
				}
				if (listadoOut.length>0)
					{
					listado=listadoOut;
					contenido+="<br><table class=\"tablaDatos\" style=\"width:100%\">";
					contenido+="<thead><tr><th><input id=\"selectAll\" type=\"checkbox\" onClick=\"toggleAll();\"></th><th>Num.</th><th>Nombre</th><th>Estado</th><th>Habitación</th><th>Tipo</th><th>Menor</th><th></th><th></th></tr></thead>";
					for (var i=0;i<listado.length;i++)
					{
					menorStr="";
					var id=listado[i]["id"];
					var nombre=listado[i]["nombre"];
					var apellidos=listado[i]["apellidos"];
					var pseudonimo=listado[i]["pseudonimo"];
					var email=listado[i]["email"];
					var nif=listado[i]["nif"];
					var menor=listado[i]["menor"];
					var estado=listado[i]["estado"];
					var grupal=listado[i]["grupal"];
					var fecha=new Date(listado[i]["fecha"]).toLocaleString();
					var pagoindividual="<input type=\"hidden\" id=\"grupal-"+id+"\" value="+grupal+">";
					var tipoHabitacion="(Sin habitación)";
					if (menor) menorStr="(Menor)";
					if (!grupal)
					{
					tipoHabitacion="<a href=\"javascript:asignar('"+id+"');\">(Asignar habitación)</a>";
					}
					if (estado==0)
					{
					estadoStr="(Desalojado)";
					}
					else if (estado==4)
					{
					estadoStr="Pago confirmado";
					}
					else if (estado==9)
					{
					estadoStr="Baja";
					tipoHabitacion="";
					}
					linea="<tr><td><input class='seleccion' type='checkbox' id='chk-"+id+"' value='"+id+"'></td>";
					linea+="<td>"+(i+1)+"</td><td>"+nombre+" "+apellidos+"("+pseudonimo+")</td><td>"+estadoStr+"</td><td>"+tipoHabitacion+"</td><td>"+tipoHabitacion+"</td><td>"+menorStr+"</td><td>"+fecha+"</td><td><a href=\"javascript:togglePagosInscrito('"+id+"');\">Comprobar Pagos</a></td><td><a href=\"javascript:toggleAcceso('"+id+"');\"><img style=\"width:15px;\" src=\"../../img/candado.png\"></a></td></tr>";
					if (tipoHabitacion!="")
					{
					linea+="<tr id=\"asignar-"+id+"\" style=\"display:none\"><td colspan=7><b>Selecciona habitación a asignar:</b><div id=\"lista-habitaciones-"+id+"\"></div> </td></tr>";
					}
					linea+="<tr id=\"acceso-"+id+"\" style=\"display:none\"><td colspan=7><b>Usuario</b>: <input type=\"text\" id=\"usuario-"+id+"\">Contraseña:<input type=\"text\" id=\"password-"+id+"\"> <input onClick=\"actualizarAcceso('"+id+"');\" type=\"button\" value=\"Actualizar\"></td></tr>";
										linea+="<tr id=\"pagosrealizados-"+id+"\" style=\"display:none\"><td id=\"tdpagosrealizados-"+id+"\"  colspan=7></td></tr>";

					linea+="<tr id=\"pago-"+id+"\" style=\"display:none\"><td colspan=7><b>Registrar Pago/Devolución</b>: "+pagoindividual+"Importe:<input type=\"text\" id=\"importe-"+id+"\"> <input onClick=\"registrarPago('"+id+"');\" type=\"button\" value=\"Registrar\"></td></tr>";
					
					/*if (estado==0)
					{
					linea+="<tr id=\"pago-"+id+"\" style=\"display:none\"><td colspan=7><b>Registrar Pago</b>: "+pagoindividual+"Importe:<input type=\"text\" id=\"importe-"+id+"\"> Completado:<input type=\"checkbox\" id=\"chktotal-"+id+"\"><input onClick=\"registrarPago('"+id+"');\" type=\"button\" value=\"Registrar\"></td></tr>";
					}*/
					contenido+=linea;
					}
					contenido+="</table>";
					}
					if (listadoEspera.length>0)
					{
					listado=listadoEspera;
					contenido+="<br><table class=\"tablaDatos\" style=\"width:100%\">";
					contenido+="<thead><tr><th><input id=\"selectAll\" type=\"checkbox\" onClick=\"toggleAll();\"></th><th>Num.</th><th>Nombre</th><th>Estado</th><th>Lista de Espera</th><th>Tipo</th><th>Menor</th></tr></thead>";
					for (var i=0;i<listado.length;i++)
					{
					menorStr="";
					var id=listado[i]["id"];
					var nombre=listado[i]["nombre"];
					var apellidos=listado[i]["apellidos"];
					var email=listado[i]["email"];
					var nif=listado[i]["nif"];
					var menor=listado[i]["menor"];
					var pseudonimo=listado[i]["pseudonimo"];
					var estado=listado[i]["estado"];
					var grupal=listado[i]["grupal"];
					var pagoindividual="<input type=\"hidden\" id=\"grupal-"+id+"\" value="+grupal+">";
					var tipoHabitacion="(Lista de espera)";
					if (menor) menorStr="(Menor)";
					if (!grupal)
					{
					tipoHabitacion="<a href=\"javascript:asignar('"+id+"');\">(Asignar habitación)</a>";
					}
					if (estado==0)
					{
					estadoStr="Pendiente de pago <a href=\"javascript:$('#pago-"+id+"').toggle();\">(+)</a>";
					}
					else if (estado==4)
					{
					estadoStr="Pago confirmado";
					}
					else if (estado==9)
					{
					estadoStr="Baja";
					tipoHabitacion="";
					}
					linea="<tr><td><input class='seleccion' type='checkbox' id='chk-"+id+"' value='"+id+"'></td>";
					linea+="<td>"+(i+1)+"</td><td>"+nombre+" "+apellidos+"("+pseudonimo+")</td><td>"+estadoStr+"</td><td>"+tipoHabitacion+"</td><td>"+tipoHabitacion+"</td><td>"+menorStr+"</td><td><a href=\"javascript:toggleAcceso('"+id+"');\"><img style=\"width:15px;\" src=\"../../img/candado.png\"></a></td></tr>";
					if (tipoHabitacion!="")
					{
					linea+="<tr id=\"asignar-"+id+"\" style=\"display:none\"><td colspan=7><b>Selecciona habitación a asignar:</b><div id=\"lista-habitaciones-"+id+"\"></div> </td></tr>";
					}
					linea+="<tr id=\"acceso-"+id+"\" style=\"display:none\"><td colspan=7><b>Usuario</b>: <input type=\"text\" id=\"usuario-"+id+"\">Contraseña:<input type=\"text\" id=\"password-"+id+"\"> <input onClick=\"actualizarAcceso('"+id+"');\" type=\"button\" value=\"Actualizar\"></td></tr>";
					if (estado==0)
					{
					linea+="<tr id=\"pago-"+id+"\" style=\"display:none\"><td colspan=7><b>Registrar Pago</b>: "+pagoindividual+"Importe:<input type=\"text\" id=\"importe-"+id+"\"> Completado:<input type=\"checkbox\" id=\"chktotal-"+id+"\"><input onClick=\"registrarPago('"+id+"');\" type=\"button\" value=\"Registrar\"></td></tr>";
					}
					contenido+=linea;
					}
					contenido+="</table>";
					}
				$('#listado').html(contenido);
			}
			else
				{
				$('#listado').html("<H2>Por ahora no hay inscritos en el sistema.</H2>");
				}
		}
});

}
function componerListaHabitaciones(idinscrito,iddiv)
{
var html="";
$.ajax({
		url:"/Ezellohar/admin/plazas/ListPlazasDisponibles",
		type:"GET",
		data:"orden=",
		success:function(respuesta,estados){
		var listado=JSON.parse(respuesta);
		if (listado.length>0)
		{
			html="<table class=\"tablaDatos\" style=\"width:100%\">";
			html+="<thead><tr><th>Num.</th><th>Capacidad</th><th>Ocupación</th><th>Planta</th><th>Nombre</th><th>Estado</th></tr></thead>";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["id"];
					var estado=listado[i]["estado"];
					if (estado==0)
					{
						estadoStr="<b>Inicial</b>";
					}
					else if (estado==1)
					{
						estadoStr="<b>Disponible</b>";
					}
					else if (estado==2)
					{
						estadoStr="<b>Temporal</b>";
					}
					else if (estado==3)
					{
						estadoStr="<b>Reservada</b>";
					}
					else if (estado==4)
					{
						estadoStr="<b>Pagada</b>";
					}
					else if (estado==9)
					{
						estadoStr="<b>Habilitada para Inscr. Individuales</b>";
					}
					else if (estado==8)
					{
						estadoStr="<b>Bloqueada</b>";
					}
					var estilo="";
					var plazas=listado[i]["plazas"]
					var camas = listado[i]["camas"];
					var ocupacion = listado[i]["ocupacion"];
					if ((ocupacion!=plazas) && (estado==3 || estado==4)) estilo="style=\"background-color:red\"";
					var nombre=listado[i]["identificador"];
					var planta=listado[i]["planta"];
					if (planta=="") planta="(sin asignar)";
					if (nombre=="") nombre="(sin nombre)";
					
					if (estado==9)
					{
					linea="<tr>";
					linea+="<td><input type=\"button\" id=\"show-"+idh+"\" onClick=\"$('#divparcial-"+idh+"').toggle();\" value=\"Ver\"><input onClick=\"cargarOcupantes('"+idh+"');\" type=\"button\" value=\"Ver ocupantes\"></td><td>"+listado[i]["plazas"]+" personas ("+camas+" camas)</td><td "+estilo+">"+ocupacion+"</td><td><a href='javascript:asignarPlanta(\""+idh+"\",\""+planta+"\");'>"+planta+"</a></td><td><a href='javascript:asignarNombre(\""+idh+"\",\""+nombre+"\");'>"+nombre+"</a></td><td>"+estadoStr+"</td>";
					}
					else
					{
					linea="<tr>";
					linea+="<td>";
					if (listado[i]["plazas"]>ocupacion)
					{
					linea+="<input onClick=\"asignarHabitacion('"+idinscrito+"','"+idh+"',0);\" type=\"button\" value=\"Asignar\">";
					}
					else
					{
					linea+="(Completa)";
					}
					linea+="<input onClick=\"cargarOcupantes('"+idh+"');\" type=\"button\" value=\"Ver ocupantes\">";
					linea+="</td><td>"+listado[i]["plazas"]+" personas ("+camas+" camas)</td><td "+estilo+">"+ocupacion+"</td><td><a href='javascript:asignarPlanta(\""+idh+"\",\""+planta+"\");'>"+planta+"</a></td><td><a href='javascript:asignarNombre(\""+idh+"\",\""+nombre+"\");'>"+nombre+"</a></td><td>"+estadoStr+"<div id=\"ocupantesHabitacion-"+idh+"\" style=\"display:none\"></div></td>";
					}


					linea+="</tr>";
					if (estado==9)
					{
					
					linea+="<tr><td colspan=7 style=\"border:0px\"><div id=\"ocupantesHabitacion-"+idh+"\" style=\"display:none\"></div>";
					linea+="<div><div class=\"div-parcial\" id=\"divparcial-"+idh+"\" style=\"display:none\"></div></div></td></tr>";
					}
					linea+="</tr>";

					html+=linea;
					}
			}
		html+="</table>";
		$(iddiv).html(html);
		cargarListadoParciales(idinscrito);
		}
		}
		);
}
function cargarOcupantes(id)
{

$.ajax({
		url:"/Ezellohar/admin/plazas/VerOcupantes",
		data:"idHabitacion="+id,
		type:"GET",
		success:function(respuesta,estados){
		$("#ocupantesHabitacion-"+id).html(respuesta);
		$("#ocupantesHabitacion-"+id).show();
		}
		});
}

function asignarNombre(id,nombre)
{
var nombre=window.prompt("Introduzca el nombre de la habitación:",nombre);
$.ajax({
		url:"/Ezellohar/CambiarNombreHabitacion",
		type:"POST",
		data:"id="+id+"&nombre="+nombre,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				cargarListado("");
				}
			else
				{
				alert("Error al cambiar el nombre: "+respObj["resultado"]);
				}
		}
});
}

function asignar(id)
{
$("#asignar-"+id).toggle();
if ($("#asignar-"+id).is(":visible"))
{
componerListaHabitaciones(id,"#lista-habitaciones-"+id);
}
else
{
$("#lista-habitaciones-"+id).html("");
}

}

function asignarHabitacion(idinscrito,idhabitacion,tipo)
{
$.ajax({
		url:"/Ezellohar/admin/plazas/AsignarHabitacion",
		type:"POST",
		data:"idinscrito="+idinscrito+"&idhabitacion="+idhabitacion+"&tipo="+tipo,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Habitación asignada");
				cargarListado("");
				}
			else
				{
				alert("Error al asignar la habitación: "+respObj["resultado"]);
				}
		}
		});
}


function registrarPago(id)
{

var objeto=JSON.parse("{}");
objeto["idInscrito"]=id;
objeto["importe"]=$("#importe-"+id).val();
objeto["completado"]=$("#chktotal-"+id).is(':checked');
objeto["grupal"]=$("#grupal-"+id).val();
$.ajax({
		url:"/Ezellohar/admin/pagos/RegistrarPago",
		type:"POST",
		data:objeto,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Pago registrado");
				cargarListado("");
				}
			else
				{
				alert("Error al registrar el pago: "+respObj["resultado"]);
				}
		}
		});
}

 function QRCheckin(usuario)
 {
	window.open("/Ezellohar/admin/inscritos/checkin.html?usuario="+usuario);
 }
 function cargarCheckin(usuario)
 {
 $.ajax({
		url:"/Ezellohar/GenerarCheckin",
		data:"usuario="+usuario,
		type:"GET",
		success:function(respuesta,estados){
				$("#datosiden").html(respuesta);
		}
});
 }

function cargarListadoParciales(idinscrito)
{

$.ajax({
		url:"/Ezellohar/admin/plazas/ListPlazasParcialesDisponibles",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var esdoble="";
			var contenido="";
			var estadoStr="";
			var idRaiz="";
			var idRaizAnt="";
			if (listado.length>0)
			{
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["idHabitacion"];
					var estado=listado[i]["estado"];
					idRaiz=listado[i]["idRaiz"];
					if (idRaiz!=idRaizAnt) 
					{
						$('#divparcial-'+idRaizAnt).html($('#divparcial-'+idRaizAnt).html()+linea);
						linea="";
						idRaizAnt=idRaiz;
					}
					if (estado==0)
					{
						estadoStr="<b>Desactivada</b>";
					}
					else if (estado==1)
					{
						estadoStr="<b>Disponible</b>";
					}
					else if (estado==2)
					{
						estadoStr="<b>Temporal</b>";
					}
					else if (estado==3)
					{
						estadoStr="<b>Reservada</b>";
					}
					else if (estado==4)
					{
						estadoStr="<b>Pagada</b>";
					}
					else if (estado==8)
					{
						estadoStr="<b>Bloqueada</b>";
					}
					if (estado<=1)
					{
					linea+="<input onClick=\"asignarHabitacion('"+idinscrito+"','"+idh+"',1);\" type=\"button\" value=\"Asignar\">: ";
					}
					else
					{
					linea+="(Ya asignada): ";
					}
					linea+=idh+": "+estadoStr+"<br>";
					}
					if (linea!="") 
					{
						$('#divparcial-'+idRaizAnt).html($('#divparcial-'+idRaizAnt).html()+linea);
						linea="";
						idRaizAnt=idRaiz;
					}
			}

		}
});
	
}