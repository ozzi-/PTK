<?php
  $logPath = "log.json";
  $lb = "\r\n";
  $id = (isset($_GET["id"]))?$_GET["id"]:"unknown";
  $id = base64_decode($id);
  $id = (($id===FALSE ||!ctype_print($id))?"unknown":$id);
  $datetime = date("Y-m-d H:i:s", time());
  $ip = $_SERVER["REMOTE_ADDR"];

  $entry["datetime"]=$datetime;
  $entry["ip"]=$ip;
  $entry["id"]=$id;

  if(!file_exists($logPath)){
    file_put_contents($logPath, '[]');
  }

  $inp = file_get_contents($logPath);
  $tempArray = json_decode($inp);
  array_push($tempArray, $entry);
  $jsonData = json_encode($tempArray);
  file_put_contents($logPath, $jsonData);
?>
