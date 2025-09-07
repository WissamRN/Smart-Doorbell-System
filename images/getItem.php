
<?php
require_once 'init.php';
//$query = "select id from admin where id>0";
$max_query = "SELECT MAX(id) FROM notifications" ;



if($return=mysqli_query($con,$max_query)){
$result=mysqli_fetch_row($return);

$key = $result[0];
$query="select image FROM notifications where id = '$key'";
$query1="select date FROM notifications where id = '$key'";




if($return1=mysqli_query($con,$query)){
	$result1 = mysqli_fetch_row($return1);
	$re=$result1[0];
	$return_date=mysqli_query($con,$query1);
	$date = mysqli_fetch_row($return_date);
	
	//$encoded_data = base64_encode(file_get_contents($re));
	//$im=file_get_contents($result1);
	//$image= base64_encode($im);
	
	echo json_encode(array('response'=>$key,'image'=>$re,'date'=>$date));
}
else
	echo 'connection error';

}
else
	echo 'connection error1';
?>
