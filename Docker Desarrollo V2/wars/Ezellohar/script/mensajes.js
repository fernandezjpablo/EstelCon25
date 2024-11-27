/**
 * 
 */




function generarMensaje()
{
$.ajax({
		url:"/Ezellohar/admin/mensajeria/GenerarMensajeSaliente",
		type:"POST",
		data:"masivopendientes="+$("#masivopendientes").is(':checked')+"&masivo="+$("#masivo").is(':checked')+"&plantilla="+$('#plantilla option:selected').val()+"&destino="+$("#destino").val()+"&asunto="+$("#asunto").val()+"&data="+encodeURIComponent(tinymce.get('cuerpomensaje').getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Mensaje generado");
				cargarListado();
				}
			else
				{
				alert("Error al generar el mensaje: "+respObj["mensaje"]);
				}
		}
});
}


function newMensaje(id)
{
$("#asunto").val("");
$("#destino").val("");
$("#cuerpomensaje").html("");
$("#formularioMensaje").toggle();
	tinymce.init({
		  selector: 'textarea#cuerpomensaje',
		  convert_urls:false,
		  paste_data_images:true,
		});
	tinymce.activeEditor.setContent('');

}

function editPlantillas()
{
$("#seleccionPlantillas").toggle();
$("#contenidoPlantilla").hide();
try
{
tinymce.get('cuerpoplantilla').setContent('');
}
catch (e)
{}
}

function editPlantilla(id)
{
$("#contenidoPlantilla").show();
$("#nombrePlantilla").val(id);
$.ajax({
		url:"/Ezellohar/admin/mensajeria/GetCuerpoPlantilla",
		type:"GET",
		data:"plantilla="+id,
		success:function(respuesta,estados){
			$("#cuerpoplantilla").html(respuesta);
			tinymce.init({
		  selector: 'textarea#cuerpoplantilla',
		  menubar:'insert format',
		  plugins:'link',
		  paste_data_images:true,
		  convert_urls:false,
		  fontsize_formats: '8pt 10pt 12pt 14pt 16pt 18pt 24pt 36pt 48pt',
  		font_formats: 'Arial=arial,helvetica,sans-serif; Courier New=courier new,courier,monospace; AkrutiKndPadmini=Akpdmi-n'
		});
		tinymce.activeEditor.ui.registry.getAll().buttons;
		tinymce.get('cuerpoplantilla').setContent(respuesta)
		}
});
}
function leerMensajeID(ruta)
{
$.ajax({
		url:"/Ezellohar/admin/mensajeria/LeerCorreo",
		type:"POST",
		data:"ruta="+encodeURIComponent(ruta),
		success:function(respuesta,estados){
			var ventana=window.open();
			$(ventana.document.body).html(respuesta);
		}
});
}
function guardarPlantilla()
{
$.ajax({
		url:"/Ezellohar/admin/mensajeria/GuardarPlantilla",
		type:"POST",
		data:"tipo="+$("#nombrePlantilla").val()+"&data="+encodeURIComponent(tinymce.get('cuerpoplantilla').getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Plantilla actualizada");
				}
			else
				{
				alert("Error al actualizar la plantilla: "+respObj["resultado"]);
				}
		}
});
}

function enviarMensajes()
{
var resp=confirm("¿Seguro que quieres enviar los mensajes seleccionados?");
if (resp)
{
var mensajes=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	mensajes.push(elemento.value);
});
$("#notificacion").html("Procesando petición. Un momento, por favor...");
$.ajax({
		url:"/Ezellohar/admin/mensajeria/AccionesMensajes",
		type:"POST",
		data:"accion=lanzar&mensajes="+mensajes,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Mensajes enviados");
				$("#notificacion").html("");
				cargarListado();
				}
			else
				{
				alert("Error al enviar los mensajes: "+respObj["resultado"]);
				}
		}
});
 }
}


function recuperarMensajes()
{
var resp=confirm("¿Seguro que quieres recuperar a pendientes los mensajes seleccionados?");
if (resp)
{
var mensajes=JSON.parse("[]");
$('.seleccionEnviados:checkbox:checked').each(function(i,elemento) {
	mensajes.push(elemento.value);
});
$('.seleccionErroneos:checkbox:checked').each(function(i,elemento) {
	mensajes.push(elemento.value);
});

$.ajax({
		url:"/Ezellohar/admin/mensajeria/AccionesMensajes",
		type:"POST",
		data:"accion=recuperar&mensajes="+mensajes,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Mensajes recuperados");
				cargarListado();
				}
			else
				{
				alert("Error al recuperar los mensajes: "+respObj["resultado"]);
				}
		}
});
 }
}

function eliminarMensajes()
{
var resp=confirm("¿Seguro que quieres eliminar los mensajes seleccionados?");
if (resp)
{
resp=confirm("¿Totalmente segur@?");
if (resp)
{
var mensajes=JSON.parse("[]");
$('.seleccionPendientes:checkbox:checked').each(function(i,elemento) {
	mensajes.push(elemento.value);
});
$('.seleccionEnviados:checkbox:checked').each(function(i,elemento) {
	mensajes.push(elemento.value);
});
$('.seleccionErroneos:checkbox:checked').each(function(i,elemento) {
	mensajes.push(elemento.value);
});

$.ajax({
		url:"/Ezellohar/admin/mensajeria/AccionesMensajes",
		type:"POST",
		data:"accion=eliminar&mensajes="+mensajes,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Mensajes eliminados");
				cargarListado();
				}
			else
				{
				alert("Error al eliminar los mensajes: "+respObj["resultado"]);
				}
		}
});
 }
}
}

function toggleAll(tipo)
{
var resultado=$("#selectAll"+tipo).is(':checked');
$(".seleccion"+tipo).each(function(){
$(this).prop("checked",resultado);
})
}

function ejecutarTarea()
{
var tarea=$('#tarea option:selected').val();
if (tarea=="eliminar")
{
eliminarMensajes();
}
else if (tarea=="recuperar")
{
recuperarMensajes();
}
else if (tarea=="lanzar")
{
enviarMensajes();
}
}


function formatearListado(listado,tituloMensajes,tipoMensajes)
{
			if (listado.length>0)
			{
			contenido="Mensajes "+tituloMensajes+"<br><table class=\"tablaDatos\" style=\"width:100%\">";
			contenido+="<thead><th><input id=\"selectAll"+tipoMensajes+"\" type=\"checkbox\" onClick=\"toggleAll('"+tipoMensajes+"');\"></th><th>Creado</th><th>Actualizado</th><th>Asunto</th><th>Destinatarios</th><th></th></thead>";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["archivo"];
					var ruta=listado[i]["ruta"];
						var fecha=listado[i]["fecha"];
					var fechaUpdate=listado[i]["fechaUpdate"];
					var destinatarios="";
					if (listado[i]["to"]!="") destinatarios+=listado[i]["to"]+";";
					if (listado[i]["cc"]!="") destinatarios+=listado[i]["cc"]+";";
					if (listado[i]["bcc"]!="") destinatarios+=listado[i]["bcc"]+";";
					
					var asunto=listado[i]["asunto"];
					linea="<tr><td><input class='seleccion"+tipoMensajes+"' type='checkbox' id='chk-"+idh+"' value='"+ruta+"'></td>";
					var fecha=listado[i]["fecha"];
					var fechaUpdate=listado[i]["fechaUpdate"];
					linea+="<td>"+fecha+"</td><td>"+fechaUpdate+"</td><td><b>"+asunto+"</b> </td><td>"+destinatarios+"</td>";
					
					contenido+=linea;
					linea="<td><input type=\"button\" onClick=\"leerMensajeID('"+ruta.replaceAll("\\","\\\\")+"');\" value=\"Leer\"></td></tr>";
					contenido+=linea;
					}
					contenido+="</table>";
				return contenido;
			}
			else
			{
			return "<table class=\"tablaDatos\" style=\"width:100%\"><tr><td>No hay mensajes "+tipoMensajes+"</td></tr></table>";
			}
			
}

function cargarListado()
{

$.ajax({
		url:"/Ezellohar/admin/mensajeria/ListMensajesAdmin",
		type:"GET",
		success:function(respuesta,estados){
			var objeto=JSON.parse(respuesta);
			
			var linea="";
			var contenido="";
			
			var listado=objeto["pendientes"];
			contenido+=formatearListado(listado,"Pendientes","Pendientes");
			
			listado=objeto["erroneos"];
			contenido+=formatearListado(listado,"Erróneos","Erroneos");
			listado=objeto["enviados"];
			contenido+=formatearListado(listado,"Enviados","Enviados");
			

			
			$('#listado').html(contenido);

		}
});
	
}