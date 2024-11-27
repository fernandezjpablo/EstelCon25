/**
 * 
 */

function eliminarNoticias()
{
var resp=confirm("¿Seguro que quieres eliminar las noticias seleccionadas?");
if (resp)
{
resp=confirm("¿Totalmente segur@?");
if (resp)
{
var noticias=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	noticias.push(elemento.value);
});

$.ajax({
		url:"/Ezellohar/admin/noticias/EliminarNoticias",
		type:"POST",
		data:"noticias="+noticias,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Noticias eliminadas");
				cargarListado();
				}
			else
				{
				alert("Error al eliminar las noticias: "+respObj["resultado"]);
				}
		}
});
 }
}
}

function estadoNoticiasSeleccionadas(estado)
{
var resp=confirm("Seguro que quieres cambiar el estado de las noticias seleccionadas?");
if (resp)
{
var noticias=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	noticias.push(elemento.value);
});

$.ajax({
		url:"/Ezellohar/admin/noticias/UpdateEstadoNoticias",
		type:"POST",
		data:"noticias="+noticias+"&estado="+estado,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Actualizado");
				cargarListado();
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


function toggleAll()
{
var resultado=$("#selectAll").is(':checked');
$(".seleccion").each(function(){
$(this).prop("checked",resultado);
})
}

function guardarNoticia()
{
$.ajax({
		url:"/Ezellohar/admin/noticias/GuardarNoticia",
		type:"POST",
		data:"titulo="+$("#titulo").val()+"&data="+encodeURIComponent(tinymce.get('cuerponoticia').getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Noticia generada");
				cargarListado();
				}
			else
				{
				alert("Error al registrar la noticia: "+respObj["resultado"]);
				}
		}
});
}
function guardarCuerpoNoticiaID(id)
{
$.ajax({
		url:"/Ezellohar/admin/noticias/UpdateCuerpoNoticia",
		type:"POST",
		data:"id="+id+"&data="+encodeURIComponent(tinymce.get('cuerponoticia-'+id).getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Noticia actualizada");
				cargarListado();
				}
			else
				{
				alert("Error al actualizar la noticia: "+respObj["resultado"]);
				}
		}
});
}
function guardarNoticiaID(id)
{
var datos=JSON.parse("{}");
var contenido=encodeURI(tinymce.get('cuerponoticia-'+id).getContent());
datos["cuerpo"]="";
datos["id"]=id;
datos["titulo"]=encodeURIComponent($("#titulo-"+id).val());
datos=JSON.stringify(datos);
$.ajax({
		url:"/Ezellohar/admin/noticias/UpdateNoticia",
		type:"POST",
		data:datos,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				guardarCuerpoNoticiaID(id);
				}
			else
				{
				alert("Error al actualizar la noticia: "+respObj["resultado"]);
				}
		}
});

}
function editNoticiaID(id)
{
	$("#noticia-"+id).toggle();
	tinymce.init({
		selector: 'textarea#cuerponoticia-'+id,
		convert_urls:false,
		plugins:'link',
		paste_data_images:true,
	});
}

function editNoticia()
{
$("#titulo").val("");
$("#formularioNoticias").toggle();
	tinymce.init({
		  selector: 'textarea#cuerponoticia',
		  convert_urls:false,		  
		  plugins:'link',
		  paste_data_images:true,
		});
	tinymce.activeEditor.setContent('');
	

}

function ejecutarTarea()
{
var tarea=$('#tarea option:selected').val();
if (tarea=="eliminar")
{
eliminarNoticias();
}
else if (tarea=="publicar")
{
estadoNoticiasSeleccionadas(1);
}
else if (tarea=="deshabilitar")
{
estadoNoticiasSeleccionadas(0);
}
else if (tarea=="archivar")
{
estadoNoticiasSeleccionadas(9);
}
}


function cargarListado()
{

$.ajax({
		url:"/Ezellohar/admin/noticias/ListNoticiasAdmin",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var contenido="";
			if (listado.length>0)
			{
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["idNoticia"];
					var estado=listado[i]["estado"];
					if (estado==0)
					{
						estado="<b>No publicada</b>";
					}
					else if (estado==1)
					{
						estado="<b>Publicada</b>";
					}
					else if (estado==2)
					{
						estado="<b>Archivada</b>";
					}
					linea="<input class='seleccion' type='checkbox' id='chk-"+idh+"' value='"+idh+"'>";
					var fecha=listado[i]["fecha"];
					var fechaUpdate=listado[i]["fechaUpdate"];
					linea+=fecha+" ("+fechaUpdate+") <b>"+listado[i]["titulo"]+"</b> . Estado de publicación: "+estado+"<br>";
					contenido+=linea;
					linea="<a href=\"javascript:editNoticiaID('"+idh+"');\">Editar Noticia</a><div id=\"noticia-"+idh+"\" class=\"observaciones\"><input type=\"text\" size=100 id=\"titulo-"+idh+"\" value=\""+listado[i]["titulo"]+"\"><br><textarea id=\"cuerponoticia-"+idh+"\">"+decodeURI(listado[i]["cuerpo"])+"</textarea><input type=\"button\" onClick=\"guardarNoticiaID('"+idh+"');\" value=\"Guardar\"></div><br>";
					contenido+=linea;
					}
				$('#listado').html(contenido);
			}
			else
				{
				$('#listado').html("<H2>No hay noticias creadas.</H2>");
				}
		}
});
	
}