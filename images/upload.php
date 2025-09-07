

<?php

include 'init.php';

$image=$_POST['image'];
$name=$_POST['name'];
$upload_path = "imageuploads/$name.jpg";
$sql="insert into commands (image,name,command,date) values('$image','$name','addImage',CURRENT_TIMESTAMP)";

if (mysqli_query($con,$sql)){
	
	//file_put_contents($upload_path, base64_decode($image));
	
	echo json_encode(array('response'=>'image uploaded successfully'));
}
else {
	echo json_encode(array('response'=>'image did not upload'));
}

mysqli_close($con);



?>
