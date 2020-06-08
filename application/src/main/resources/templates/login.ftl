<#ftl encoding='UTF-8'>

<div class="row align-items-center">
    <div class="col-xs-2 col-sm-4"></div>
    <div class="col-xs-8 col-sm-4 my-auto">
        <#if RequestParameters.status??>
            <p>아이디와 비밀번호를 확인해 주세요</p>
        </#if>
        <form class="form-signin" method="post" action="/loginProc">
          <h2 class="form-signin-heading">Please sign in</h2>
          <p>
            <label for="username" class="sr-only">Username</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus>
          </p>
          <p>
            <label for="password" class="sr-only">Password</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
          </p>
          <p><input type='checkbox' name='remember-me'/> Remember me on this computer.</p>
          <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        </form>
    </div>
    <div class="col-xs-2 col-sm-4"></div>
</div>

