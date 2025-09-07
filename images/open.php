<?php

include 'init.php';


$comand="open";
$sql=	"insert into commands (command) values ('open')";
if (mysqli_query($con,$sql))
	
echo "success";
	else 
		echo 'fail';
	
	
	
/*
$image=$_POST['image'];
$name=$_POST['name'];
$upload_path = "imageuploads/$name.jpg";
$sql="insert into notifications(image,description) values('$image','$name')";

if (mysqli_query($con,$sql)){
	
	file_put_contents($upload_path, base64_decode($image));
	
	echo json_encode(array('response'=>'image uploaded successfully'));
}
else {
	echo json_encode(array('response'=>'image did not upload'));
}

mysqli_close($con);
*/


?>