<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
<title>Scam Detector</title>
</head>
<body>
  	  
  	  <!-- header -->
	  <header>
      <!-- Fixed navbar -->
      <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand" href="/scam_detector_sample/main">UCI Scam Detector</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
              <a class="nav-link" href="/scam_detector_sample/main">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="">How to use</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="https://uci.edu/">UCI home page</a>
            </li>
          </ul>
          <form class="form-inline mt-2 mt-md-0">
            <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
          </form>
        </div>
      </nav>
    </header>


    <!-- Begin page content -->
    <main role="main" class="container">
		<br>
		<h1 class="mt-5">Scam Detector Test Page</h1>
		<hr>
		<p>
			Put a sentence or paragraph you want to detect scam
		</p>
		<br>
		
		<div align = "center">
			<form action = "/scam_detector_sample/detect_scam" method = "post">
	    		<textarea name = "user_input" class="form-control" id="exampleFormControlTextarea1" rows="4" placeholder="This is user input area">${user_input}</textarea>
				<br>
				<button type="submit" class="btn btn-primary">Detect!</button>
				<br><br>
	    		<textarea name = "result" class="form-control" id="exampleFormControlTextarea1" rows="4"  placeholder="This is result area">${result}</textarea>
			</form>
		</div>
		    
    </main>
	
	
	<!-- footer -->
	<div class="fixed-bottom p-3 mb-2 bg-secondary text-white">
	    <footer class="footer">
	      <div class="container">
	        <span class="text-white">
	        	Scam detector example is © Chan Cheong. This is UCI UROP sample site<br>
	        	Visit the scam detector home page!<br>
	        	Contact the developer!<br>
	        	Name | Chan Cheong<br>
	        	Major | Computer engineering, Dankook University<br>
	        	Email | j94chan@gmail.com
	        </span>
	      </div>
	    </footer>
	</div>

    <!-- Bootstrap core JavaScript
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
  </body>
</html>