/**
 * 
 */
 

function actualizarObservaciones(idactividad,idusuario)
{
	var resp=confirm("¿Confirmas que quieres actualizar estas observaciones?");
	if (resp)
	{
		$.ajax({
		url:"/Ezellohar/admin/actividades/ActualizarObservacionesInscrito",
		type:"POST",
		data:"idInscrito="+idusuario+"&idActividad="+idactividad+"&observaciones="+$("#obser-"+idactividad+"-"+idusuario).val(),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["respuesta"]=="ok")
				{
				alert("Observaciones actualizadas");
				cargarListadoActividadesInscritos();
				}
			else
				{
				alert("Error al actualizar las observaciones : "+respObj["respuesta"]);
				}
		}
});
	}
	
} 

 
function registrarPagoActividad(idactividad,idusuario)
{
	var resp=confirm("¿Confirma que quiere registrar este pago?");
	if (resp)
	{
		$.ajax({
		url:"/Ezellohar/admin/pagos/RegistrarPagoActividad",
		type:"POST",
		data:"idInscrito="+idusuario+"&idActividad="+idactividad+"&accion=1&importe="+$("#pago-"+idactividad+"-"+idusuario).val(),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Pago registrado");
				cargarListadoActividadesInscritos();
				}
			else
				{
				alert("Error al registrar el pago : "+respObj["resultado"]);
				}
		}
});
	}
	
} 

function consultarPagosActividades(idactividad,idusuario)
{
$("#pagos-"+idactividad+"-"+idusuario).toggle();
} 
 
function anularPagoActividad(idactividad,idusuario)
{
	var resp=confirm("¿Confirma que quiere anular este pago?");
	if (resp)
	{
		$.ajax({
		url:"/Ezellohar/admin/pagos/RegistrarPagoActividad",
		type:"POST",
		data:"idInscrito="+idusuario+"&idActividad="+idactividad+"&accion=0&importe="+$("#pago-"+idactividad+"-"+idusuario).val(),
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Pago anulado");
				cargarListadoActividadesInscritos();
				}
			else
				{
				alert("Error al anular el pago : "+respObj["resultado"]);
				}
		}
});
	}
	
}  
 
function calcularResponsables()
{

var responsables = "";
var radiobuttons = $("input[type='radio'][name='radioResponsable']:checked");
if (radiobuttons.length > 0) {
    responsables = radiobuttons.val();
}
$("#responsablesActividad").val(responsables);
}

function cargarListadoActividadesInscritos()
{
$.ajax({
		url:"/Ezellohar/admin/actividades/ListadoActividadesInscripciones",
		type:"POST",
		data:"id="+$("#idActividad").val(),
		success:function(respuesta,estados){
				$('#gestionInscritos').html(respuesta);

		}
});
}

function gestionInscritos()
{
$('#gestionInscritos').toggle();
if ($('#gestionInscritos').is(":visible"))
{
cargarListadoActividadesInscritos();
}
}

function verCalendario()
{
$('#calendarioPlanificado').toggle();
if ($('#calendarioPlanificado').is(":visible"))
{
$.ajax({
		url:"/Ezellohar/admin/planificacion/VerCalendarioPlanificacion",
		type:"GET",
		data:"id="+$("#idActividad").val(),
		success:function(respuesta,estados){
				$('#calendarioPlanificado').html(respuesta);

		}
});
}
}

function verCalendarioPonentes()
{
$('#calendarioPlanificado').toggle();
if ($('#calendarioPlanificado').is(":visible"))
{
$.ajax({
		url:"/Ezellohar/admin/planificacion/VerCalendarioPonentes",
		type:"GET",
		data:"id="+$("#idActividad").val(),
		success:function(respuesta,estados){
				$('#calendarioPlanificado').html(respuesta);

		}
});
}
}

function marcarResponsables(responsables)
{
if (responsables.indexOf("]")>0)
{
var responsables=JSON.parse(responsables);
responsables=responsables[0];
}
	$('#chkresp-'+responsables).prop("checked",true);
}

function cargarCamposActividad(objeto)
{
	/*data:"
	pseudonimos="+$('#pseudonimosActividad').val()+"&
	publico="+$('#publicoActividad option:selected').val()+"&
	pago="+$('#pagoAdicional').is(':checked')+"&
	descripcion="+encodeURIComponent($('#descripcionActividad').html(),"UTF-8"),
	*/
	$("#descripcionActividad").html(objeto["descripcion"]);
	$("#requisitosActividad").html(objeto["requisitos"]);
	$("#observacionesActividad").html(objeto["observaciones"]);
	$("#tipoActividad").val(objeto["tipo"]);
	$("#responsablesActividad").val(objeto["responsables"]);
	marcarResponsables(objeto["responsables"]);
	$("#pseudonimosActividad").val(objeto["nombres_responsables"]);
	$("#aforoActividad").val(objeto["aforo"]);
	$("#duracionActividad").val(objeto["duracion"]);
	$("#publicoActividad").val(objeto["publico"]);
	$("#nombreActividad").val(objeto["nombreActividad"]);
	if (objeto["pagoAdicional"]) $("#pagoAdicional").prop("checked",true);
}

function cargarCamposEspacio(objeto)
{
	/*data:"


	pseudonimos="+$('#pseudonimosActividad').val()+"&
	publico="+$('#publicoActividad option:selected').val()+"&
	pago="+$('#pagoAdicional').is(':checked')+"&
	descripcion="+encodeURIComponent($('#descripcionActividad').html(),"UTF-8"),
	*/
	$("#descripcionEspacio").html(objeto["descripcion"]);
	$("#capacidadEspacio").val(objeto["aforo"]);
	$("#idEspacio").val(objeto["idEspacio"]);
	$("#nombreEspacio").val(objeto["nombreEspacio"]);
	$("#plantaEspacio").val(objeto["planta"]);
}

function limpiarCamposActividad()
{
	$("#formAltasActividades")[0].reset();	
	$("#observacionesActividad").html("");
	$("#requisitosActividad").html("");
}

function editarActividad(id)
{
	$("#idActividad").val(id);
	$("#formularioAltasActividades").show();
	limpiarCamposActividad();
$.ajax({
		url:"/Ezellohar/admin/actividades/ConsultarActividad",
		type:"POST",
		data:"id="+$("#idActividad").val(),
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
					cargarCamposActividad(respObj);
					$("#btnRegistrar").hide();
					$("#btnActualizar").show();
					tinymce.activeEditor.setContent(respObj["descripcion"]);
		},
		error:function(respuesta,estados){
					alert("Error al consultar la actividad, revise los campos.");
					cargarListadoActividades();
		},
		failure:function(respuesta,estados)
		{
					alert("Error al consultar la actividad, revise los campos.");
					cargarListadoActividades();			
		}
});

}


function editarEspacio(id)
{
	$("#idEspacio").val(id);
	$("#formularioAltasEspacios").show();
	$("#formAltasEspacios")[0].reset();	
$.ajax({
		url:"/Ezellohar/admin/espacios/ConsultarEspacio",
		type:"POST",
		data:"id="+$("#idEspacio").val(),
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
					cargarCamposEspacio(respObj);
					$("#btnRegistrarEspacio").hide();
					$("#btnActualizarEspacio").show();
		},
		error:function(respuesta,estados){
					alert("Error al consultar el espacio, revise los campos.");
					cargarListadoActividades();
		},
		failure:function(respuesta,estados)
		{
					alert("Error al consultar el espacio, revise los campos.");
					cargarListadoActividades();			
		}
});

}


function actualizarActividad()
{
var alertstr="";
calcularResponsables();
if ($('#nombreActividad').val()=="") alertstr+="Debe indicar un nombre para la actividad.\n";
if ($('#tipoActividad').val()=="") alertstr+="Debe indicar qué tipo de actividad es.\n";
if ($('#aforoActividad').val()=="") alertstr+="Debe indicar un aforo para la actividad.\n";
if ($('#duracionActividad').val()=="") alertstr+="Debe indicar duración para la actividad.\n";
if ($('#responsablesActividad').val()=="[]") alertstr+="Debe seleccionar responsable de la actividad.\n";
if ($('#publicoActividad option:selected').val()=="") alertstr+="Seleccione el público objetivo de la actividad.\n";
if (alertstr=="")
{
$.ajax({
		url:"/Ezellohar/admin/actividades/ActualizarActividad",
		type:"POST",
		data:"id="+$("#idActividad").val()+"&nombre="+$('#nombreActividad').val()+"&duracion="+$('#duracionActividad').val()+"&responsables="+$('#responsablesActividad').val()+"&aforo="+$('#aforoActividad').val()+"&requisitos="+$('#requisitosActividad').val()+"&observaciones="+$('#observacionesActividad').val()+"&tipo="+$('#tipoActividad').val()+"&pseudonimos="+$('#pseudonimosActividad').val()+"&publico="+$('#publicoActividad option:selected').val()+"&pago="+$('#pagoAdicional').is(':checked')+"&descripcion="+encodeURIComponent(tinymce.activeEditor.getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
				if (respObj["resultado"]=="ok")
					{
					alert("Actividad actualizada");
					limpiarCamposActividad();
					cargarListadoActividades();
					$("#btnRegistrar").show();
					$("#btnActualizar").hide();
					$("#formularioAltasActividades").hide();
					}

		},
		error:function(respuesta,estados){
					alert("Error al actualizar la actividad, revise los campos.");
					cargarListadoActividades();
		},
		failure:function(respuesta,estados)
		{
					alert("Error al actualizar la actividad, revise los campos.");
					cargarListadoActividades();			
		}
});
}
else
{
	alert(alertstr);
}
	
}

function guardarActividad()
{
var alertstr="";
calcularResponsables();
if ($('#nombreActividad').val()=="") alertstr+="Debe indicar un nombre para la actividad.\n";
if ($('#aforoActividad').val()=="") alertstr+="Debe indicar un aforo para la actividad.\n";
if ($('#tipoActividad').val()=="") alertstr+="Debe indicar qué tipo de actividad es.\n";
if ($('#duracionActividad').val()=="") alertstr+="Debe indicar una duración estimada para la actividad.\n";
if ($('#responsablesActividad').val()=="[]") alertstr+="Debe seleccionar responsable de la actividad.\n";
if ($('#publicoActividad option:selected').val()=="") alertstr+="Seleccione el público objetivo de la actividad.\n";
if (alertstr=="")
{
$.ajax({
		url:"/Ezellohar/admin/actividades/NuevaActividad",
		type:"POST",
		data:"nombre="+$('#nombreActividad').val()+"&requisitos="+$('#requisitosActividad').val()+"&observaciones="+$('#observacionesActividad').val()+"&tipo="+$('#tipoActividad').val()+"&responsables="+$('#responsablesActividad').val()+"&duracion="+$('#duracionActividad').val()+"&aforo="+$('#aforoActividad').val()+"&pseudonimos="+$('#pseudonimosActividad').val()+"&publico="+$('#publicoActividad option:selected').val()+"&pago="+$('#pagoAdicional').is(':checked')+"&descripcion="+encodeURIComponent(tinymce.activeEditor.getContent().replace(/\+/g,"%2B")),
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
				if (respObj["resultado"]=="ok")
					{
					alert("Actividad creada");
					limpiarCamposActividad();
					cargarListadoActividades();
					}

		},
		error:function(respuesta,estados){
					alert("Error al crear la actividad, revise los campos.");
					cargarListadoActividades();
		},
		failure:function(respuesta,estados)
		{
					alert("Error al crear la actividad, revise los campos.");
					cargarListadoActividades();			
		}
});
}
else
{
	alert(alertstr);
}
	
}

function actualizarEspacio()
{
var alertstr="";
if ($('#nombreEspacio').val()=="") alertstr+="Debe indicar un nombre para el espacio.\n";
if ($('#capacidadEspacio').val()=="") alertstr+="Debe indicar un aforo para el espacio.\n";
if ($('#plantaEspacio').val()=="") alertstr+="Debe indicar la planta.\n";
if (alertstr=="")
{
$.ajax({
		url:"/Ezellohar/admin/espacios/ActualizarEspacio",
		type:"POST",
		data:"id="+$('#idEspacio').val()+"&nombre="+$('#nombreEspacio').val()+"&capacidad="+$('#capacidadEspacio').val()+"&planta="+$('#plantaEspacio').val()+"&descripcion="+encodeURIComponent($('#descripcionEspacio').val(),"UTF-8"),
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
				if (respObj["resultado"]=="ok")
					{
					alert("Espacio actualizado");
					$("#formAltasEspacios")[0].reset();	
					$("#descripcionEspacio").val("");	
					cargarListadoEspacios();
					
					}

		},
		error:function(respuesta,estados){
					alert("Error al crear el espacio, revise los campos.");
					cargarListadoActividades();
		},
		failure:function(respuesta,estados)
		{
					alert("Error al crear el espacio, revise los campos.");
					cargarListadoActividades();			
		}
});
}
else
{
	alert(alertstr);
}
	
}

function guardarEspacio()
{
var alertstr="";
if ($('#nombreEspacio').val()=="") alertstr+="Debe indicar un nombre para la actividad.\n";
if ($('#capacidadEspacio').val()=="") alertstr+="Debe indicar un aforo para la actividad.\n";
if ($('#plantaEspacio').val()=="") alertstr+="Debe indicar los usuarios de lso responsables de la actividad.\n";
if (alertstr=="")
{
$.ajax({
		url:"/Ezellohar/admin/espacios/NuevoEspacio",
		type:"POST",
		data:"nombre="+$('#nombreEspacio').val()+"&capacidad="+$('#capacidadEspacio').val()+"&planta="+$('#plantaEspacio').val()+"&descripcion="+encodeURIComponent($('#descripcionEspacio').val(),"UTF-8"),
		success:function(respuesta,estados){
				var respObj=JSON.parse(respuesta);
				if (respObj["resultado"]=="ok")
					{
					alert("Espacio creado");
					$("#formAltasEspacios")[0].reset();	
					$("#descripcionEspacio").val("");	
					cargarListadoEspacios();
					
					}

		},
		error:function(respuesta,estados){
					alert("Error al crear el espacio, revise los campos.");
					cargarListadoActividades();
		},
		failure:function(respuesta,estados)
		{
					alert("Error al crear el espacio, revise los campos.");
					cargarListadoActividades();			
		}
});
}
else
{
	alert(alertstr);
}
	
}


function filtrarActividades()
{
//$('#cmbActividades option:selected').val()
var publico=$('#filtroPublico option:selected').val();
var tipo=$('#filtroTipos option:selected').val();
$.ajax({
		url:"/Ezellohar/admin/actividades/ListarActividadesFiltro",
		type:"GET",
		data:"publico="+publico+"&tipo="+tipo+"&noplanificadas="+$("#filtroPlanificadas").is(':checked'),
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var comboActividades="";
			var linea="";
			var esdoble="";
			var contenido="";
			var estadoStr="";
			var idRaiz="";
			var idRaizAnt="";
			if (listado.length>0)
			{
				comboActividades="<select id=\"cmbActividades\">";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["idActividad"];
					var estado=listado[i]["estado"];
					var nombre=listado[i]["nombreActividad"];
					var aforo=listado[i]["aforo"];
					var publico=listado[i]["publico"];
					var duracion=listado[i]["duracion"];
					var descripcion=listado[i]["descripcion"];
					var tipo=listado[i]["tipo"];
					if (typeof tipo === "undefined") tipo="";
					if (tipo=="") tipo="(Sin especificar)";
					if (duracion=="") duracion="(Sin especificar)";
					if (estado==0)
					{
						estadoStr="<b>No planificada: Sin inscripción</b>";
					}
					else if (estado==1)
					{
						estadoStr="<b>No planificada: Con Inscripción</b>";
					}
					else if (estado==4)
					{
						estadoStr="<b>Planificada: Sin Inscripción</b>";
					}
					else if (estado==5)
					{
						estadoStr="<b>Planificada: Con Inscripción</b>";
					}
					else if (estado==9)
					{
						estadoStr="<b>Propuesta recibida</b>";
					}
					comboActividades+="<option id=\"opt-"+idh+"\" value=\""+nombre+"\">"+nombre+"(aforo:"+aforo+") "+estadoStr+" ("+publico+")("+duracion+")</option>";
					}
					contenido+="</table>";
					comboActividades+="</select>";

				
			}
				$("#selActividades").html(comboActividades);

		}
});
	
}



function cargarComboTipos()
{
$.ajax({
		url:"/Ezellohar/admin/actividades/ListarTiposActividades",
		type:"GET",
		success:function(respuesta,estados){
		var listado=JSON.parse(respuesta);
		var comboTipos="";
		var comboTiposListado="";
		if (listado.length>0)
		{
			comboTipos="Tipo de Actividad:<select id=\"filtroTipos\" onChange=\"filtrarActividades();\">";
			comboTipos+="<option value=\"\">Todos los tipos</option>";
			comboTiposListado="Tipo de Actividad:<select id=\"filtroTiposListado\" onChange=\"cargarListadoActividades();\">";
			comboTiposListado+="<option value=\"\">Todos los tipos</option>";
			
			 
			
			for (var i=0;i<listado.length;i++)
			{
				comboTipos+="<option value=\""+listado[i]+"\">"+listado[i]+"</option>";
				comboTiposListado+="<option value=\""+listado[i]+"\">"+listado[i]+"</option>";
			}
			comboTipos+="</select>";
			comboTiposListado+="</select>";
		}
		$("#selFiltroTipo").html(comboTipos);
		$("#selFiltroTipoListado").html(comboTiposListado);
		}
		});
}

function cargarListadoActividades()
{
var publico=$('#filtroPublicoListado option:selected').val();
if (typeof publico === "undefined") publico="";
var tipo=$('#filtroTiposListado option:selected').val();
if (typeof tipo === "undefined") tipo="";
$.ajax({
		url:"/Ezellohar/admin/actividades/ListarActividadesFiltro",
		type:"GET",
		data:"publico="+publico+"&tipo="+tipo+"&noplanificadas="+$("#filtroPlanificadas"+tipo).is(':checked'),		
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var comboActividades="";
			var linea="";
			var esdoble="";
			var contenido="";
			var estadoStr="";
			var idRaiz="";
			var idRaizAnt="";
			if (listado.length>0)
			{
				comboActividades="<select id=\"cmbActividades\">";
			contenido="<table class=\"tablaDatos\" style=\"width:100%\">";
			contenido+="<thead><tr><th><input id=\"selectAllActividades\" type=\"checkbox\" onClick=\"toggleAll('Actividades');\"></th><th>Nombre</th><th>Tipo</th><th>Aforo</th><th>Duración estimada</th><th>Público</th><th>Estado</th><th></th></tr></thead>";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["idActividad"];
					var estado=listado[i]["estado"];
					var nombre=listado[i]["nombreActividad"];
					var aforo=listado[i]["aforo"];
					var publico=listado[i]["publico"];
					var duracion=listado[i]["duracion"];
					var descripcion=listado[i]["descripcion"];
					var tipo=listado[i]["tipo"];
					if (typeof tipo === "undefined") tipo="";
					if (tipo=="") tipo="(Sin especificar)";
					if (duracion=="") duracion="(Sin especificar)";
					if (estado==0)
					{
						estadoStr="<b>No planificada: Sin inscripción</b>";
					}
					else if (estado==1)
					{
						estadoStr="<b>No planificada: Con Inscripción</b>";
					}
					else if (estado==4)
					{
						estadoStr="<b>Planificada: Sin Inscripción</b>";
					}
					else if (estado==5)
					{
						estadoStr="<b>Planificada: Con Inscripción</b>";
					}
					else if (estado==9)
					{
						estadoStr="<b>Propuesta recibida</b>";
					}
					linea="<tr><td><input type=\"checkbox\" id=\"chk-"+idh+"\" class=\"seleccionActividades\" value=\""+idh+"\"></td><td>"+nombre+"</td><td>"+tipo+"</td><td>"+aforo+"</td><td>"+duracion+"</td><td>"+publico+"</td><td>"+estadoStr+"</td><td><input type=\"button\" onClick=\"editarActividad('"+idh+"');\" value=\"Editar\"></td></tr>";
					contenido+=linea;
					comboActividades+="<option id=\"opt-"+idh+"\" value=\""+nombre+"\">"+nombre+"(aforo:"+aforo+") "+estadoStr+" ("+publico+")</option>";
					}
					contenido+="</table>";
					comboActividades+="</select>";
				$("#listadoActividadesInterno").html(contenido);
				$("#selActividades").html(comboActividades);
				
			}
			else
			{
				$("#listadoActividadesInterno").html("");
			}

		}
});
	
}


function cargarListadoResponsables()
{

$.ajax({
		url:"/Ezellohar/ListInscritos",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			listado=JSON.parse(listado["alojados"]);
			var contenido="";
			var pseudonimoStr="";
			if (listado.length>0)
			{
				for (var i=0;i<listado.length;i++)
				{
					if (listado[i]["pseudonimo"]!="")
					{
						pseudonimoStr="("+listado[i]["pseudonimo"]+")";
					}
					contenido+="<input name=\"radioResponsable\" type=\"radio\" id='chkresp-"+listado[i]["id"]+"' value='"+listado[i]["id"]+"' class=\"selResponsable\">"+listado[i]["nombre"]+" "+listado[i]["apellidos"]+pseudonimoStr+"<br>";
				}
				$("#listaResponsables").html(contenido);
				
			}
			else
			{
				$("#listaResponsables").html("");
			}

		}
});
	
}

function registrarPlanificacion()
{
$("#notificacionesTabla").html("<span style=\"color:red\">Cargando planificación, espere por favor...</span><br><br>");
$.ajax({
		url:"/Ezellohar/admin/planificacion/RegistrarPlanificacion",
		type:"POST",
		data:$("#planificacionActual").val(),
		success:function(respuesta,estados){
			alert("Planificación actualizada");
			$("#notificacionesTabla").html("");
			filtrarActividades();
		}
});
	
}

function removeCasillaActividad(id)
{
	
	/*
	<input class=\"selplani\" type=\"checkbox\" id=\"chkplani-fecha-"+fechas[i]+"-espacio-"+espacios[e]+"-tiempo-"+h+"-intervalo-"+intervalo+"\" onClick=\"addCasillaActividad('fecha-"+fechas[i]+"-espacio-"+espacios[e]+"-tiempo-"+h+"-intervalo-"+intervalo+"');\">
	*/
	var planificacion=JSON.parse($("#planificacionActual").val());
	delete planificacion["planificadas"][id];
	planificacion["desplanificadas"][id]="{\"id\":\""+$('#cmbActividades option:selected').prop("id")+"\",\"color\":\""+$('#colores option:selected').val()+"\",\"actividad\":\""+$('#cmbActividades option:selected').val()+"\"}";
	$("#planificacionActual").val(JSON.stringify(planificacion));
	$("#celda-"+id).html("<input class=\"selplani\" type=\"checkbox\" id=\"chkplani-"+id+"\" onClick=\"addCasillaActividad('"+id+"');\">");
}

$(document).ready(function(){
	var planificacion=JSON.parse($("#planificacionActual").val());
	if (!planificacion.hasOwnProperty("planificadas")) 
	{
		planificacion["planificadas"]=JSON.parse("{}");
	}
	if (!planificacion.hasOwnProperty("desplanificadas")) 
	{
		planificacion["desplanificadas"]=JSON.parse("{}");
	}
	$("#planificacionActual").val(JSON.stringify(planificacion));
});

function addCasillaActividad(id)
{
	var planificacion=JSON.parse($("#planificacionActual").val());
	delete planificacion["desplanificadas"][id];
	planificacion["planificadas"][id]="{\"id\":\""+$('#cmbActividades option:selected').prop("id")+"\",\"color\":\""+$('#colores option:selected').val()+"\",\"actividad\":\""+$('#cmbActividades option:selected').val()+"\"}";
	$("#planificacionActual").val(JSON.stringify(planificacion));
	$("#celda-"+id).html("<span onClick=\"removeCasillaActividad('"+id+"');\" style=\"background-color:"+$('#colores option:selected').val()+"\">"+$('#cmbActividades option:selected').val()+"</span>");
}

function generarTablaPlanificacionAjax()
{

	var fechaInicio=new Date($("#fechaInicio").val());
	var fechaFin=new Date($("#fechaFin").val());
	var espacios=JSON.parse("[]");
	var nomespacios=JSON.parse("[]");
	var horaInicio=$("#horaInicio option:selected").val();
	var horaFin=$("#horaFin option:selected").val();
	var intervalo=$("#intervalo option:selected").val()*1;
	$('.selPlaniEspacios:checkbox:checked').each(function(i,elemento) {
	espacios.push(elemento.value.split(":")[0]);
	nomespacios.push(elemento.value.split(":")[1]);
	}
	);

$("#notificacionesTabla").html("<span style=\"color:red\">Cargando planificación, espere por favor...</span><br><br>");
	$.ajax({
		url:"/Ezellohar/admin/planificacion/ObtenerTablaPlanificacion",
		type:"GET",
		data:"fechaInicio="+fechaInicio.getTime()+"&fechaFin="+fechaFin.getTime()+"&horaInicio="+horaInicio+"&horaFin="+horaFin+"&espacios="+espacios+"&intervalo="+intervalo+"&nomespacios="+nomespacios,
		success:function(respuesta,estados){
			$("#tablaPlanificacion").html(respuesta);
			$("#notificacionesTabla").html("");
			}
		});
	
}

function cargarListadoEspacios()
{

$.ajax({
		url:"/Ezellohar/admin/espacios/ListarEspacios",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var contenidoSeleccion="";
			var esdoble="";
			var contenido="";
			var estadoStr="";
			var idRaiz="";
			var idRaizAnt="";
			if (listado.length>0)
			{
			contenido="<table class=\"tablaDatos\" style=\"width:100%\">";
			contenido+="<thead><tr><th><input id=\"selectAllEspacios\" type=\"checkbox\" onClick=\"toggleAll('Espacios');\"></th><th>Nombre</th><th>Aforo</th><th>Planta</th><th></th></tr></thead>";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["idEspacio"];
					var estado=listado[i]["estado"];
					var nombre=listado[i]["nombreEspacio"];
					var aforo=listado[i]["aforo"];
					var planta=listado[i]["planta"];
					var descripcion=listado[i]["descripcion"];
					if (estado==0)
					{
						estadoStr="<b>Desactivado</b>";
					}
					else if (estado==1)
					{
						estadoStr="<b>Disponible</b>";
					}
					linea="<tr><td><input type=\"checkbox\" id=\"chk-"+idh+"\" class=\"seleccionEspacios\" value=\""+idh+"\"></td><td>"+nombre+"</td><td>"+aforo+"</td><td>"+planta+"</td><td><input type=\"button\" onClick=\"editarEspacio('"+idh+"');\" value=\"Editar\"></td></tr>";
					contenido+=linea;
					contenidoSeleccion+="<input type=\"checkbox\" id=\"chk-"+idh+"\" class=\"selPlaniEspacios\" value=\""+idh+":"+nombre+"("+aforo+" plazas)\"></td><td>"+nombre+"("+aforo+" plazas)<br>";
					}
				contenido+="</table>";
				$("#listadoEspacios").html(contenido);
				$("#seleccionEsp").html(contenidoSeleccion);
			}
			else
			{
				$("#listadoEspacios").html("");
			}

		}
});
	
}


function toggleAll(tipo)
{
var resultado=$("#selectAll"+tipo).is(':checked');
$(".seleccion"+tipo).each(function(){
$(this).prop("checked",resultado);
})
}

function desplanificarActividades()
{
	var resp=confirm("¿Seguro que quieres desplanificar las actividades seleccionadas?");
	if (resp)
	{
		var elementos=JSON.parse("[]");
		$('.seleccionActividades:checkbox:checked').each(function(i,elemento) {
		elementos.push(elemento.value);
		});
		$.ajax({
		url:"/Ezellohar/admin/planificacion/DesplanificarActividades",
		type:"POST",
		data:"ids="+elementos,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Actividades desplanificadas");
				cargarListadoActividades();
				cargarListadoEspacios();
				}
			else
				{
				alert("Error al desplanificar actividades : "+respObj["resultado"]);
				}
		}
});
	}
	
}

function estadoActividades(accion)
{
	var resp=confirm("¿Seguro que quieres "+accion+" las actividades seleccionadas?");
	if (resp)
	{
		var elementos=JSON.parse("[]");
		$('.seleccionActividades:checkbox:checked').each(function(i,elemento) {
		elementos.push(elemento.value);
		});
		$.ajax({
		url:"/Ezellohar/admin/actividades/EstadoActividades",
		type:"POST",
		data:"accion="+accion+"&ids="+elementos,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Elementos modificados");
				cargarListadoActividades();
				cargarListadoEspacios();
				}
			else
				{
				alert("Error al "+accion+" actividades : "+respObj["resultado"]);
				}
		}
});
	}
	
}


function estadoInscritos(accion)
{
	var resp=confirm("¿Seguro que quieres "+accion+" los inscritos seleccionadas?");
	if (resp)
	{
		var elementos=JSON.parse("[]");
		$('.inscrito:checkbox:checked').each(function(i,elemento) {
		elementos.push(elemento.value);
		});
		$('.espera:checkbox:checked').each(function(i,elemento) {
		elementos.push(elemento.value);
		});
		$.ajax({
		url:"/Ezellohar/admin/actividades/CambiarEstadoInscritosActividad",
		type:"POST",
		data:"accion="+accion+"&ids="+elementos,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Elementos modificados");
				cargarListadoActividades();
				cargarListadoEspacios();
				gestionInscritos();
				}
			else
				{
				alert("Error al "+accion+" inscritos : "+respObj["resultado"]);
				}
		}
});
	}
	
}
function eliminar(tipo)
{
var resp=confirm("¿Seguro que quieres eliminar las actividades seleccionadas?");
if (resp)
{
resp=confirm("¿Totalmente segur@?");
if (resp)
{
var elementos=JSON.parse("[]");
$('.seleccion'+tipo+':checkbox:checked').each(function(i,elemento) {
	elementos.push(elemento.value);
});
if (tipo=='Actividades')
{
	direccion="/Ezellohar/admin/actividades/EliminarActividades";
}
else if (tipo=='Espacios')
{
	direccion="/Ezellohar/admin/espacios/EliminarEspacios";
}
$.ajax({
		url:direccion,
		type:"POST",
		data:"ids="+elementos,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Elementos eliminados");
				cargarListadoActividades();
				cargarListadoEspacios();
				}
			else
				{
				alert("Error al eliminar "+tipo+": "+respObj["resultado"]);
				}
		}
});
 }
}
}

function limpiarPlanificacion()
{
	var resp=confirm("¿Seguro que quieres eliminar toda la planificación?");
if (resp)
{
resp=confirm("¿Totalmente segur@?");
if (resp)
{
	$.ajax({
		url:"/Ezellohar/admin/planificacion/EliminarPlanificacion",
		type:"POST",
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Planificación eliminada");
				cargarListadoActividades();
				cargarListadoEspacios();
				}
			else
				{
				alert("Error al eliminar la planificación: "+respObj["mensaje"]);
				}
		}
});
}
}
}
function planificacionPaso1()
{
	$("#tablaPlanificacion").html("");
	$("#planificacionPaso2").hide();
	$("#planificacionPaso1").show();
}
function planificacionPaso2()
{
if ($('.selPlaniEspacios:checkbox:checked').length>0)
{
	$("#planificacionActual").val("{\"planificadas\":{},\"desplanificadas\":{}}");
	generarTablaPlanificacionAjax();
	cargarListadoActividades();
	$("#planificacionPaso1").hide();
	$("#planificacionPaso2").show();
}
else
{
alert("Es necesario seleccionar al menos un espacio para planificar");
}
}
function ejecutarTarea(menu)
{
var tarea=$('#tarea'+menu+' option:selected').val();
if (tarea=="eliminarActividades")
{
eliminar('Actividades');
}
else if (tarea=="eliminarEspacios")
{
eliminar('Espacios');
}
else if (tarea=="borrar")
{
limpiarPlanificacion();
}
else if (tarea=="desplanificar")
{
desplanificarActividades();
}
else if (tarea=="activarActividades")
{
estadoActividades('activar');
}
else if (tarea=="desactivarActividades")
{
estadoActividades('desactivar');
}
else if (tarea=="aceptarActividades")
{
estadoActividades('aceptar');
}
else if (tarea=="eliminarInscripcion")
{
estadoInscritos('eliminar');
}
else if (tarea=="listaEspera")
{
estadoInscritos('espera');
}
else if (tarea=="inscribirEspera")
{
estadoInscritos('inscribir');
}
else if (tarea=="exportar")
{
window.open("/Ezellohar/admin/actividades/ListadoActividadesExportar");
}
else if (tarea=="inscritos")
{
window.open("/Ezellohar/admin/actividades/ListadoActividadesExportarInscripciones");
}
else if (tarea=="inscritosweb")
{
window.open("/Ezellohar/admin/actividades/ListadoActividadesExportarInscripcionesWeb");
}
}