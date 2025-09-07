<?php

include 'init.php';


$comand="open";
$sql=	"insert into commands (command) values ('capture')";
if (mysqli_query($con,$sql))
	
echo "success";
	else 
		echo 'fail';
	
	
	



?>