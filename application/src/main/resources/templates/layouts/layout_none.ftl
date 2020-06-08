<#ftl encoding='UTF-8'>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title><@tiles.getAsString name="title"/></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    
    <link type="text/css" rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div class="row">
    <div class="col-1"></div>
    <div class="col-10">
    <@tiles.insertAttribute name="section"/>
    </div>
    <div class="col-1"></div>
</div>
</body>
</html>
