
function inicioGrupos()
{
var respuesta=confirm("Este tipo de inscripción es para habitaciones INDIVIDUALES NO COMPARTIDAS o GRUPOS COMPLETOS. Necesitarás TODOS LOS DATOS de los ocupantes. Si es lo que quieres pulsa Aceptar para continuar.");
if (respuesta)
{
lanzarPaso1();
$('#btnGrupo').prop('disabled',true);
$('#btnIndividual').prop('disabled',true);
$('#btnGrupo').css('cursor','not-allowed');
$('#btnIndividual').css('cursor','not-allowed');
$('#info').hide();
}
}

function inicioIndividual()
{
var respuesta=confirm("Este tipo de inscripción es para una inscripción individual en una habitación COMPARTIDA con compañer@s seleccionados aleatoriamente. Si es lo que quieres pulsa Aceptar para continuar.");
if (respuesta)
{
lanzarPaso1individual();
$('#btnGrupo').prop('disabled',true);
$('#btnIndividual').prop('disabled',true);
$('#btnGrupo').css('cursor','not-allowed');
$('#btnIndividual').css('cursor','not-allowed');
$('#info').hide();
}
}


function inicioListaEspera()
{
lanzarPaso1ListaEspera();
$('#btnListaEspera').prop('disabled',true);
$('#btnListaEspera').css('cursor','not-allowed');
$('#info').hide();

}
function cargarListado()
{

$.ajax({
		url:"/Valimar/ListNoticiasPublish",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var contenido="";
			if (listado.length>0)
			{
				for (var i=0;i<listado.length;i++)
					{
					var fecha=listado[i]["fecha"];
					var fechaUpdate=listado[i]["fechaUpdate"];
					linea="<div class=\"titulonoticia\"><span class=\"fecha\">"+fecha+"</span><br> <span class\"titulo\"><a href=\"javascript:$('#noticia-"+listado[i]["idNoticia"]+"').toggle();\">"+listado[i]["titulo"]+"</a></span></div>";
					linea+="<div class=\"cuerponoticia\" style=\"display:none\" id=\"noticia-"+listado[i]["idNoticia"]+"\"><a href=\"viewpost.html?id="+listado[i]["idNoticia"]+"\" target=_blank>Abrir en otra ventana</a><br>"+listado[i]["cuerpo"]+"</div>";
					contenido+=linea;
					}
				$('#noticias').html(contenido);
			}
			else
				{
				$('#noticias').html("<H2>Estamos trabajando en ello...</H2>");
				}
		}
});
	
}

function reiniciarProceso()
{
var confirmar=confirm("¿Se perderán los datos cumplimentados, quiere reiniciar el proceso?");
if (confirmar)
{
	location.reload();
}
}

function lanzarPaso1()
{
	cargarComboPaso1();
	$('#paso1').show();
}

function toggle(id)
{
	if ($('#es_menor_'+id).prop('checked'))
	{
		$('#iden_'+id).prop('disabled',true);
	}
	else
		{
		$('#iden_'+id).prop('disabled',false);
	}
	
}
function enviarInscripcion()
{
	var numItems=$('#comboHabitaciones').find('option:selected').val().split(',')[0];
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	var objeto;
	for (var i=0;i<numItems;i++)
	{
		objeto=JSON.parse("{}");
		objeto["iden"]=$('#iden_'+(i+1)).val();
		objeto["nombre"]=$('#nombre_'+(i+1)).val();
		objeto["apellidos"]=$('#apellidos_'+(i+1)).val();
		objeto["pseudonimo"]=$('#pseudonimo_'+(i+1)).val();
		objeto["email"]=$('#email_'+(i+1)).val();
		objeto["telefono"]=$('#telefono_'+(i+1)).val();
		objeto["lopd"]=$('#lopd'+(i+1)).prop('checked');
		if (i==0) 
		{
		objeto["menor"]=false;
		objeto["con_bebes"]=$('#fecha_bebe_'+(i+1)+' option:selected').val();
		} 
		else
		{
		objeto["menor"]=$('#es_menor_'+(i+1)).prop('checked');
		objeto["con_bebes"]=0;
		}
		
	lista.push(objeto);
	}

		$.ajax({
			url:"/Valimar/RegistrarInscripcion",
			type:"POST",
			data:"habitacion="+$("#idhabitacion").val()+"&datos="+JSON.stringify(lista),
			success:function(respuesta,estados){
					$("#paso3").hide();
					$("#paso2").hide();
					$("#paso4").show();
					$("#paso4").html(respuesta);
				}
	
		});		



}

function enviarInscripcionIndividual()
{
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	var objeto;
	objeto=JSON.parse("{}");
	objeto["iden"]=$('#iden').val();
	objeto["nombre"]=$('#nombre').val();
	objeto["apellidos"]=$('#apellidos').val();
	objeto["pseudonimo"]=$('#pseudonimo').val();
	objeto["email"]=$('#email').val();
	objeto["telefono"]=$('#telefono').val();
	objeto["lopd"]=$('#lopd').prop('checked');
	objeto["menor"]=false;
	objeto["parcial"]=true;
	objeto["con_bebes"]=false;
	lista.push(objeto);

		$.ajax({
			url:"/Valimar/RegistrarInscripcion",
			type:"POST",
			data:"habitacion="+$("#idhabitacion").val()+"&datos="+JSON.stringify(lista),
			success:function(respuesta,estados){
					$("#paso3individual").hide();
					$("#paso1individual").hide();
					$("#paso4").show();
					$("#paso4").html(respuesta);
				}
	
		});		



}

function enviarInscripcionEspera()
{
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	var objeto;
	objeto=JSON.parse("{}");
	objeto["iden"]=$('#iden').val();
	objeto["nombre"]=$('#nombre').val();
	objeto["apellidos"]=$('#apellidos').val();
	objeto["pseudonimo"]=$('#pseudonimo').val();
	objeto["email"]=$('#email').val();
	objeto["telefono"]=$('#telefono').val();
	objeto["lopd"]=$('#lopd').prop('checked');
	objeto["menor"]=false;
	objeto["parcial"]=true;
	objeto["con_bebes"]=0;
	lista.push(objeto);

		$.ajax({
			url:"/Valimar/RegistrarInscripcion",
			type:"POST",
			data:"habitacion=(Lista de espera)&datos="+JSON.stringify(lista),
			success:function(respuesta,estados){
					$("#paso3espera").hide();
					$("#paso4").show();
					$("#paso4").html(respuesta);
					
				}
	
		});		



}

function disableEnviarIndividual()
{
	$('#enviarIndividual').prop('disabled',true);
}

function disableEnviarEspera()
{
	$('#enviarEspera').prop('disabled',true);
}

function disableEnviar()
{
	$('#enviar').prop('disabled',true);
}
function generarPaso3(capacidad, idhabitacion)
{
	
	var elemDatos=document.getElementById("paso3Datos");
	$("#paso3Datos").html("");
	for (var i=0;i<capacidad;i++)
	{
		var divinscrito=document.createElement("div");
		var linea_menor="";
		if (i>0) {linea_menor="<input type='checkbox' id='es_menor_"+(i+1)+"' onClick=toggle('"+(i+1)+"');>Menor entre 2 y 12 años";}
		if (i==0) {
		linea_menor="<input type='checkbox' id='con_bebes_"+(i+1)+"' onClick=\"$('#fecha_bebe_"+(i+1)+"').toggle();\">Voy con menor/es de 2 años (no ocupan plaza)";
		linea_menor+="<br><select style=\"display:none\" id=\"fecha_bebe_"+(i+1)+"\"><option value=0>(Año de nacimiento del menor)</option><option value=2022>2022</option><option value=2023>2023</option><option value=2024>2024</option></select>";
		}
		divinscrito.innerHTML="<h2>Datos del inscrito n&uacute;mero "+(i+1)+"</h2><br>"+
		"Nombre: <input type='text' id='nombre_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"Apellidos: <input type='text' id='apellidos_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"Pseudónimo (opcional): <input type='text' id='pseudonimo_"+(i+1)+"' onChange='disableEnviar();'><br>"+
		"email: <input type='text' id='email_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"Tel&eacute;fono: <input type='text' id='telefono_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"NIF/NIE/Pasaporte: <input type='text' id='iden_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"<input type='checkbox' id='lopd_"+(i+1)+"' onChange='disableEnviar();'>He leído y acepto las <a href=\"terminos.html\" target=_blank>condiciones de inscripción al evento</a> (incluidas las referentes a uso de mi imagen)*<br>"+
		linea_menor+
		"<br>(*) Campos obligatorios (salvo NIF en menores de 12 años)<br>";
		elemDatos.appendChild(divinscrito);
	}
	
	$('#paso3').show();
}

function generarPaso3individual(capacidad, idhabitacion)
{
	
	var elemDatos=document.getElementById("paso3individualDatos");
	$("#paso3individualDatos").html("");
		var divinscrito=document.createElement("div");
		divinscrito.innerHTML="<h2>Datos de inscripci&oacute;n</h2><br>"+
		"Nombre: <input type='text' id='nombre' onChange='disableEnviarIndividual();'>*<br>"+
		"Apellidos: <input type='text' id='apellidos' onChange='disableEnviarIndividual();'>*<br>"+
		"Pseudónimo (opcional): <input type='text' id='pseudonimo' onChange='disableEnviarIndividual();'><br>"+
		"email: <input type='text' id='email' onChange='disableEnviarIndividual();'>*<br>"+
		"Tel&eacute;fono: <input type='text' id='telefono' onChange='disableEnviarIndividual();'>*<br>"+
		"NIF/NIE/Pasaporte: <input type='text' id='iden' onChange='disableEnviarIndividual();'>*<br>"+
		"<input type='checkbox' id='lopd' onChange='disableEnviarIndividual();'>He leído y acepto las <a href=\"terminos.html\" target=_blank>condiciones de inscripción al evento</a> (incluidas las referentes a uso de mi imagen)*"+
		"<br>(*) Campos obligatorios<br>";

		
		elemDatos.appendChild(divinscrito);

	$('#paso3individual').show();
}

function generarPaso3ListaEspera(capacidad)
{
	
	var elemDatos=document.getElementById("paso3EsperaDatos");
	$("#paso3EsperaDatos").html("");
		var divinscrito=document.createElement("div");
		divinscrito.innerHTML="<h2>Datos de inscripci&oacute;n</h2><br>"+
		"Nombre: <input type='text' id='nombre' onChange='disableEnviarEspera();'>*<br>"+
		"Apellidos: <input type='text' id='apellidos' onChange='disableEnviarEspera();'>*<br>"+
		"Pseudónimo (opcional): <input type='text' id='pseudonimo' onChange='disableEnviarEspera();'><br>"+
		"email: <input type='text' id='email' onChange='disableEnviarEspera();'>*<br>"+
		"Tel&eacute;fono: <input type='text' id='telefono' onChange='disableEnviarEspera();'>*<br>"+
		"NIF/NIE/Pasaporte: <input type='text' id='iden' onChange='disableEnviarEspera();'>*<br>"+
		"<input type='checkbox' id='lopd' onChange='disableEnviarEspera();'>He leído y acepto las <a href=\"terminos.html\" target=_blank>condiciones de inscripción al evento</a> (incluidas las referentes a uso de mi imagen)*"+
		"<br>(*) Campos obligatorios<br>";

		
		elemDatos.appendChild(divinscrito);

	$('#paso3EsperaDatos').show();
}


function lanzarPaso2()
{
$.ajax({
		url:"/Valimar/BloquearHabitacion",
		type:"POST",
		data:"capacidad="+$('#comboHabitaciones').find('option:selected').val().split(',')[0]+"&camas="+$('#comboHabitaciones').find('option:selected').val().split(',')[1],
		success:function(respuesta,estados){
			
			if (respuesta!="")
			{
				$('#paso2').html("Tiene bloqueada temporalmente la habitaci&oacute;n "+respuesta+
						".<br>Dispone de 10 minutos para formalizar la reserva antes de que la habitaci&oacute;n "+
						"vuelva a liberarse.");
				generarPaso3($('#comboHabitaciones').find('option:selected').val().split(',')[0],respuesta);
				$('#btnPaso2').prop('disabled',true);
				$("#idhabitacion").val(respuesta);
			}
			else
				{
				$('#paso2').html("Las habitaciones de esta capacidad ya no est&aacute;n disponibles.");
				}
			$('#paso2').show();
			$('#paso1').hide();

		}
});
	
}
function bloquear_campos_individual()
{
		$('#iden').prop('disabled',true);
		$('#apellidos').prop('disabled',true);
		$('#nombre').prop('disabled',true);
		$('#pseudonimo').prop('disabled',true);
		$('#telefono').prop('disabled',true);
		$('#email').prop('disabled',true);
		$('#iden').prop('disabled',true);
		$('#lopd').prop('disabled',true);
}
function bloquear_campos()
{
	var numItems=$('#comboHabitaciones').find('option:selected').val().split(',')[0];
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	var objeto;
	for (var i=0;i<numItems;i++)
	{
		$('#iden_'+(i+1)).prop('disabled',true);
		$('#apellidos_'+(i+1)).prop('disabled',true);
		$('#nombre_'+(i+1)).prop('disabled',true);
		$('#pseudonimo_'+(i+1)).prop('disabled',true);
		$('#telefono_'+(i+1)).prop('disabled',true);
		$('#email_'+(i+1)).prop('disabled',true);
		$('#iden_'+(i+1)).prop('disabled',true);
		$('#es_menor_'+(i+1)).prop('disabled',true);
		$('#lopd_'+(i+1)).prop('disabled',true);
		$('#con_bebes_'+(i+1)).prop('disabled',true);
		$('#fecha_bebe_'+(i+1)).prop('disabled',true);
	}
}


function validarEspera()
{
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	var objeto;
	var i=0;
		objeto=JSON.parse("{}");
		objeto["iden"]=$('#iden').val();
		objeto["nombre"]=$('#nombre').val();
		objeto["apellidos"]=$('#apellidos').val();
		objeto["email"]=$('#email').val();
		objeto["telefono"]=$('#telefono').val();
		
		if ($('#nombre').val()=='') resultado_validacion=resultado_validacion+"Nombre  en blanco.\n";
		if ($('#apellidos').val()=='') resultado_validacion=resultado_validacion+"Apellidos  en blanco.\n";
		if ($('#email').val()=='') resultado_validacion=resultado_validacion+"Email  en blanco.\n";
		if ($('#telefono').val()=='') resultado_validacion=resultado_validacion+"Teléfono  en blanco.\n";
		if ($('#iden').val()=='') resultado_validacion=resultado_validacion+"NIF/NIE en blanco.\n";
		if (!$('#lopd').prop('checked')) resultado_validacion=resultado_validacion+"Debe leer y aceptar las condiciones de inscripción (LOPD).\n";
		
		lista.push(objeto);
	if (resultado_validacion=="")
	{
		$.ajax({
			url:"/Valimar/CheckInscrito",
			type:"POST",
			data:"datos="+JSON.stringify(lista),
			success:function(respuesta,estados){
					respuestaArr=JSON.parse(respuesta);
					resultado_validacion="";
					for (var j=0;j<respuestaArr.length;j++)
					{
						if (respuestaArr[j]["existe"])
						{
							resultado_validacion+=resultado_validacion+"El NIF "+respuestaArr[j]["clave"]+" ya figura inscrito en el evento.\n";
						}
					}
					if (resultado_validacion=="")
					{
						bloquear_campos_individual();
						$('#enviarEspera').prop('disabled',false);
					}
					else
					{
						alert(resultado_validacion);
					}
				}
	
		});		
	}
	else
	{
		alert(resultado_validacion);
	}


	
}

function validarIndividual()
{
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	var objeto;
	var i=0;
		objeto=JSON.parse("{}");
		objeto["iden"]=$('#iden').val();
		objeto["nombre"]=$('#nombre').val();
		objeto["apellidos"]=$('#apellidos').val();
		objeto["email"]=$('#email').val();
		objeto["telefono"]=$('#telefono').val();
		
		if ($('#nombre').val()=='') resultado_validacion=resultado_validacion+"Nombre  en blanco.\n";
		if ($('#apellidos').val()=='') resultado_validacion=resultado_validacion+"Apellidos  en blanco.\n";
		if ($('#email').val()=='') resultado_validacion=resultado_validacion+"Email  en blanco.\n";
		if ($('#telefono').val()=='') resultado_validacion=resultado_validacion+"Teléfono  en blanco.\n";
		if ($('#iden').val()=='') resultado_validacion=resultado_validacion+"NIF/NIE en blanco.\n";
		if (!$('#lopd').prop('checked')) resultado_validacion=resultado_validacion+"Debe leer y aceptar las condiciones de inscripción (LOPD).\n";
		
		lista.push(objeto);
	if (resultado_validacion=="")
	{
		$.ajax({
			url:"/Valimar/CheckInscrito",
			type:"POST",
			data:"datos="+JSON.stringify(lista),
			success:function(respuesta,estados){
					respuestaArr=JSON.parse(respuesta);
					resultado_validacion="";
					for (var j=0;j<respuestaArr.length;j++)
					{
						if (respuestaArr[j]["existe"])
						{
							resultado_validacion+=resultado_validacion+"El NIF "+respuestaArr[j]["clave"]+" ya figura inscrito en el evento.\n";
						}
					}
					if (resultado_validacion=="")
					{
						bloquear_campos_individual();
						$('#enviarIndividual').prop('disabled',false);
					}
					else
					{
						alert(resultado_validacion);
					}
				}
	
		});		
	}
	else
	{
		alert(resultado_validacion);
	}


	
}

function validar()
{
	var numItems=$('#comboHabitaciones').find('option:selected').val().split(',')[0];
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	var objeto;
	for (var i=0;i<numItems;i++)
	{
		objeto=JSON.parse("{}");
		objeto["iden"]=$('#iden_'+(i+1)).val();
		objeto["nombre"]=$('#nombre_'+(i+1)).val();
		objeto["apellidos"]=$('#nombre_'+(i+1)).val();
		objeto["email"]=$('#nombre_'+(i+1)).val();
		objeto["telefono"]=$('#nombre_'+(i+1)).val();
		if (i==0) 
		{
		objeto["menor"]=false;
		} 
		else
		{
		objeto["menor"]=$('#es_menor_'+(i+1)).prop('checked');
		}
		if ($('#nombre_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Nombre "+(i+1)+" en blanco.\n";
		if ($('#apellidos_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Apellidos "+(i+1)+" en blanco.\n";
		if ($('#email_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Email "+(i+1)+" en blanco.\n";
		if ($('#telefono_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Teléfono "+(i+1)+" en blanco.\n";
		if (($('#iden_'+(i+1)).val()=='') && (!$('#es_menor_'+(i+1)).prop('checked'))) resultado_validacion=resultado_validacion+"NIF/NIE "+(i+1)+" en blanco.\n";
		if (($('#fecha_bebe_'+(i+1)+' option:selected').val()==0) && ($('#con_bebes_'+(i+1)).prop('checked'))) resultado_validacion=resultado_validacion+"Si va con menores de 4 años debe seleccionar el año de nacimiento.\n";
		if (($('#lopd_'+(i+1)).val()=='') && (!$('#lopd_'+(i+1)).prop('checked'))) resultado_validacion=resultado_validacion+"Debe leer y aceptar las condiciones de inscripción (LOPD) para el inscrito "+i+1+".\n";
		lista.push(objeto);
	}
	if (resultado_validacion=="")
	{
		$.ajax({
			url:"/Valimar/CheckInscrito",
			type:"POST",
			data:"datos="+JSON.stringify(lista),
			success:function(respuesta,estados){
					respuestaArr=JSON.parse(respuesta);
					resultado_validacion="";
					for (var j=0;j<respuestaArr.length;j++)
					{
						if (respuestaArr[j]["existe"])
						{
							resultado_validacion+=resultado_validacion+"El NIF "+respuestaArr[j]["clave"]+" ya figura inscrito en el evento.\n";
						}
					}
					if (resultado_validacion=="")
					{
						bloquear_campos();
						$('#enviar').prop('disabled',false);
					}
					else
					{
						alert(resultado_validacion);
					}
				}
	
		});		
	}
	else
	{
		alert(resultado_validacion);
	}


	
}

function cargarEstado()
{
$.ajax({
		url:"/Valimar/CheckStatusPlazas",
		type:"GET",
		success:function(respuesta,estados){
				$('#info').html(respuesta["html"]);
				if (respuesta["listaEspera"]) 
				{
				$("#btnListaEspera").show();
				$("#btnGrupo").hide();
				$("#btnIndividual").hide();
				}
				else
				{
				$("#btnListaEspera").hide();
				$("#btnGrupo").show();
				$("#btnIndividual").show();
				}

		}
});
	
}

function cargarComboPaso1()
{
$.ajax({
		url:"/Valimar/GetComboHabitacionesDisponibles",
		type:"GET",
		success:function(respuesta,estados){
				$('#paso1').html(respuesta);

		}
});
	
}


function lanzarPaso1ListaEspera()
{


				$('#paso3EsperaTextos').html("Rellena los datos para apuntarte a la lista de espera de la EstelCon.");
				generarPaso3ListaEspera(1);
				
				$("#idhabitacion").val("");


			$('#paso3espera').show();

		}


function lanzarPaso1individual()
{
$.ajax({
		url:"/Valimar/BloquearHabitacionParcial",
		type:"POST",
		success:function(respuesta,estados){
			
			if (respuesta!="")
			{
				$('#paso3individualTextos').html("Tiene bloqueada temporalmente la habitaci&oacute;n "+respuesta+
						".<br>Dispone de 10 minutos para formalizar la reserva antes de que la plaza "+
						"vuelva a liberarse.");
				generarPaso3individual(1,respuesta);
				
				$("#idhabitacion").val(respuesta);
			}
			else
				{
				$('#paso3individual').html("En este momento no quedan plazas individuales disponibles.");
				}
			$('#paso3individual').show();

		}
});
	
}