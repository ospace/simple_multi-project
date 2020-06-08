<#ftl encoding='UTF-8'>
<!DOCTYPE html>
<html>
<head>
    <#include "common_head.ftl"/>
    <script src="/js/cmm.js?v=2"></script>
</head>
<body>
<div class="row">
    <div class="col-1"></div>
    <div class="col-10">
        <header>
            <@tiles.insertAttribute name="header"/>
        </header>
        <nav>
            <@tiles.insertAttribute name="nav"/>
        </nav>
        <section>
            <@tiles.insertAttribute name="section"/>
        </section>
        <footer>
            <@tiles.insertAttribute name="footer"/>
        </footer>
    </div>
    <div class="col-1"></div>
</div>
</body>
</html>
