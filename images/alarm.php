<?php

include 'init.php';


$comand="open";
$sql=	"insert into commands (command) values ('alarm')";
if (mysqli_query($con,$sql))
	
echo "success";
	else 
		echo 'fail';
	
	
	



?>