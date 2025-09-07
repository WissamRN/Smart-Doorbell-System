<?php

include 'init.php';



$sql=	"insert into commands (command) values ('reset')";
if (mysqli_query($con,$sql)){
	
echo "success";
}
	
	
	



?>