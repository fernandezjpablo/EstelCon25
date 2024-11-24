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
	alert("enviando");
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
		if (i>0) {linea_menor="<input type='checkbox' id='es_menor_"+(i+1)+"' onClick=toggle('"+(i+1)+"');>Es menor";}
		divinscrito.innerHTML="<h2>Datos del inscrito n&uacute;mero "+(i+1)+"</h2><br>"+
		"Nombre: <input type='text' id='nombre_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"Apellidos: <input type='text' id='apellidos_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"Pseudónimo (opcional): <input type='text' id='pseudonimo_"+(i+1)+"' onChange='disableEnviar();'><br>"+
		"email: <input type='text' id='email_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"Tel&eacute;fono: <input type='text' id='telefono_"+(i+1)+"' onChange='disableEnviar();'>*<br>"+
		"NIF/NIE/Pasaporte: <input type='text' id='iden_"+(i+1)+"' onChange='disableEnviar();'>*"+
		linea_menor+
		"<br>(*) Campos obligatorios (salvo NIF en menores)<br>";
		elemDatos.appendChild(divinscrito);
	}
	
	$('#paso3').show();
}

function lanzarPaso2()
{
$.ajax({
		url:"/Ezellohar/BloquearHabitacion",
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
			}
			else
				{
				$('#paso2').html("Las habitaciones de esta capacidad ya no est&aacute;n disponibles.");
				}
			$('#paso2').show();

		}
});
	
}

function validar()
{
	var numItems=$('#comboHabitaciones').find('option:selected').val().split(',')[0];
	var resultado_validacion="";
	var lista=JSON.parse("[]");
	for (var i=0;i<numItems;i++)
	{
		if (!$('#es_menor_'+(i+1)).prop('checked')) lista.push("'"+$('#iden_'+(i+1)).val()+"'");
		if ($('#nombre_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Nombre "+(i+1)+" en blanco.\n";
		if ($('#apellidos_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Apellidos "+(i+1)+" en blanco.\n";
		if ($('#email_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Email "+(i+1)+" en blanco.\n";
		if ($('#telefono_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"Teléfono "+(i+1)+" en blanco.\n";
		if ($('#iden_'+(i+1)).val()=='') resultado_validacion=resultado_validacion+"NIF/NIE "+(i+1)+" en blanco.\n";
	}
	if (resultado_validacion=="")
	{
		$.ajax({
			url:"/Ezellohar/CheckInscrito",
			type:"POST",
			data:"ids_legal="+lista,
			success:function(respuesta,estados){
					respuestaArr=JSON.parse(respuesta);
					for (var j=0;j<respuestaArr.length;j++)
					{
						if (respuestaArr[j]["existe"])
						{
							resultado_validacion=resultado_validacion+"El NIF "+respuestaArr[j]["clave"]+" ya figura inscrito en el evento.\n";
						}
					}
	if (resultado_validacion=="")
	{
		$('#enviar').prop('disabled',false);
	}
	else
	{
		alert(resultado_validacion);
	}
	
				}
		});		
	}

	
		if (resultado_validacion!="")
	{
		alert(resultado_validacion);
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

function cargarComboPaso1()
{
$.ajax({
		url:"/Ezellohar/GetComboHabitacionesDisponibles",
		type:"GET",
		success:function(respuesta,estados){
				$('#paso1').html(respuesta);

		}
});
	
}