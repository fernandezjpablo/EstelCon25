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

function editConfiguracion()
{
$("#contenidoConfiguracion").show();
$("#listadoLogs").hide();
$.ajax({
		url:"/Ezellohar/admin/sistema/GetConfiguracion",
		type:"GET",
		success:function(respuesta,estados){
			$("#cuerpoconfiguracion").html(respuesta);
		
		}
});
}
function leerLogID(ruta)
{
$.ajax({
		url:"/Ezellohar/LeerLog",
		type:"POST",
		data:"ruta="+encodeURIComponent(ruta),
		success:function(respuesta,estados){
		$("#divContenidoLog").show();
			$("#contenidoLog").html(respuesta);
		}
});
}
function guardarConfiguracion()
{
$.ajax({
		url:"/Ezellohar/admin/sistema/GuardarConfiguracion",
		type:"POST",
		data:"data="+$("#cuerpoconfiguracion").val(),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Contenido actualizado");
				}
			else
				{
				alert("Error al actualizar la configuracion: "+respObj["resultado"]);
				}
		}
});
}











function formatearListado(listado,tituloLogs,tipoLogs)
{
			if (listado.length>0)
			{
			$("#tituloLog").html("Logs de "+tituloLogs);
			contenido="<table class=\"tablaDatos\" style=\"width:30%\">";
			contenido+="<thead><th>Nombre</th><th></th></thead>";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["archivo"];
					var ruta=listado[i]["ruta"];




					linea="<tr>";

					linea+="<td><b>"+idh+"</b> </td>";
					
					contenido+=linea;
					linea="<td><input type=\"button\" onClick=\"leerLogID('"+ruta.replaceAll("\\","\\\\")+"');\" value=\"Leer\"></td></tr>";
					contenido+=linea;
					}
					contenido+="</table>";
					contenido+="<textarea id=\"contenidoLog\" style=\"width:500px;height:500px\"></textarea>";
				return contenido;
			}
			else
			{
						$("#tituloLog").html("Logs de "+tituloLogs);
			return "<table class=\"tablaDatos\" style=\"width:50%\"><tr><td>No hay logs de "+tipoLogs+"</td></tr></table>";
			}
			
}

function listarLogs(tipo)
{
$("#contenidoConfiguracion").hide();
$("#listadoLogs").show();
$.ajax({
		url:"/Ezellohar/ListLogs",
		data:"tipo="+tipo,
		type:"POST",
		success:function(respuesta,estados){
			var objeto=JSON.parse(respuesta);
			
			var linea="";
			var contenido="";
			
			var listado=objeto;
			contenido+=formatearListado(objeto,tipo,tipo);

			

			
			$('#listado').html(contenido);

		}
});
	
}