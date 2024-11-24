function ejecutarTarea()
{
var tarea=$('#tarea option:selected').val();
if (tarea=="exportar")
{
window.open("/Ezellohar/admin/pagos/ExportarPagos");
}
else if (tarea=="exportarpagos")
{
window.open("/Ezellohar/admin/pagos/ExportarEstadoPagos");
}
else
{
estadoMovimientos(tarea);
}
}

function estadoMovimientos(tarea)
{
var movimientos=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	movimientos.push(elemento.value);
});
$.ajax({
		url:"/Ezellohar/admin/pagos/EstadoMovimientos",
		type:"POST",
		data:"accion="+tarea+"&items="+movimientos,
		success:function(respuesta,estados){
			var resultado=JSON.parse(respuesta);
			if (resultado["resultado"]=="ok")
			{
				alert("Movimientos actualizados");
			}
			else
			{
			alert(resultado["resultado"]);
			}
			cargarListado();
		}
});
}
function newMovimiento()
{
$("#formularioMovimiento").toggle();

}
function toggleAll()
{
var resultado=$("#selectAll").is(':checked');
$(".seleccion").each(function(){
$(this).prop("checked",resultado);
});
}

function guardarMovimiento()
{
var validacion="";
if ($("#importe").val()=="") 
{
validacion+="Importe no puede estar en blanco.\n";
}
else
{
if ($("#importe").val()*1 <0)
{
validacion+="Para restar utilice importes positivos en la opción Pago o Registrar devolución.";
}
}
if ($("#observaciones").val()=="") validacion+="Concepto no puede estar en blanco.\n";
if ($("#tipo").val()=="") validacion+="Seleccione tipo de movimiento.\n";
if (validacion=="")
{
$.ajax({
		url:"/Ezellohar/admin/pagos/RegistrarMovimiento",
		type:"POST",
		data:"importe="+$("#importe").val()+"&tipo="+$("#tipo").val()+"&observaciones="+$("#observaciones").val(),
		success:function(respuesta,estados){
			var resultado=JSON.parse(respuesta);
			if (resultado["resultado"]=="ok")
			{
				alert("Movimiento registrado");
				$("#importe").val("");
			}
			else
			{
			alert(resultado["resultado"]);
			$("#observaciones").val("");
			}
			cargarListado();
		}
});
}
else
{
alert(validacion);
}
}

function cargarBalance()
{

$.ajax({
		url:"/Ezellohar/admin/pagos/GetBalance",
		type:"GET",
		success:function(respuesta,estados){
			$("#balance").html("Balance actual: <b>"+respuesta+"€</b>");
		}
});
	
}

function cargarListado()
{

$.ajax({
		url:"/Ezellohar/admin/pagos/ListPagos",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var contenido="";
			if (listado.length>0)
			{
			contenido="<table class=\"tablaDatos\" style=\"width:100%\">";
			contenido+="<thead><tr><th></th><th>Fecha Inicial</th><th>Actualizado</th><th>Tipo movimiento</th><th>Importe</th><th>Concepto</th></tr></thead>";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["idPago"];
					var estado=listado[i]["estado"];
					if (estado==1)
					{
						estado="Movimiento realizado";
					}
					else if (estado==2)
					{
						estado="Pago Realizado";
					}
					else if (estado==8)
					{
						estado="Devolución pendiente";
					}
					else if (estado==99)
					{
						estado="Movimiento anulado";
					}
					else if (estado==9)
					{
						estado="Devolución realizada";
					}
					linea="<tr><td><input class='seleccion' type='checkbox' id='chk-"+idh+"' value='"+idh+"'></td>";
					var fecha=listado[i]["fecha"];
					var fechaUpdate=listado[i]["fechaUpdate"];
					linea+="<td>"+fecha+"</td><td>"+fechaUpdate+"</td><td>"+estado+"</td><td><b>"+listado[i]["importe"]+"€</b> </td><td>"+listado[i]["observaciones"]+"</td></tr>";
					contenido+=linea;
					}
					contenido+="</table>"
				$('#listado').html(contenido);
					cargarBalance();
			}
			else
				{
				$('#listado').html("<H2>De momento no hay movimientos.</H2>");
				}
		}
});
	
}