<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="_scripts.jsp"></jsp:include>
<html lang="en">
    <head>
        <meta charset="utf-8"></meta>
        <meta content="IE=edge" http-equiv="X-UA-Compatible"></meta>
        <meta content="width=device-width, initial-scale=1" name="viewport"></meta>
        <meta content="" name="description"></meta>
        <meta content="" name="author"></meta>
        <link href="../../favicon.ico" rel="icon"></link>
        <title>
            SPINNER
        </title>
        <!-- <link rel="stylesheet" href="jumbotron.css"></link> -->
        <script src="${pageContext.request.contextPath}/resources/js/spinner-application.js"></script>
        <!-- <script src="../../assets/js/ie-emulation-modes-warning.js"></script>
        <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script> -->
        <style class="firebugResetStyles" charset="utf-8" type="text/css"></style>
    </head>
    <style>
    .wrapper{
    /* border: 5px #e4e4e4 solid; */
    }
    </style>
    <body>
        <nav class="navbar navbar-inverse navbar-fixed-top">
        <a class="navbar-brand" href="#">SPINNER TOOL</a>
        <h1></h1>
        </nav>
       <!--  <div class="jumbotron">
            <div class="container">
                
                <h1>

                        Work Under Progress

                    </h1>
                    <p>
                    The work for our database is under Progress.
                    We have used Jquery, Bootstrap, SpringMVC, Hibernate, JPA Technologies.
                    </p>
                    <p>
                        <a class="btn btn-primary btn-lg" role="button" href="#"></a>
                    </p>
                    
                </div>
            </div> -->
     
     <div class="jumbotron">       
   <div class="container">
    <div class="row">
        <form role="form">
            <div class="col-lg-6">
                <div class="well well-sm"><strong><span class="glyphicon"></span>Gene Terrain Application</strong></div>
                <div class="form-group">
                    <label for="InputName">Tool Name</label>
                    <div class="input-group">
                        <input type="text" class="form-control" name="InputName" id="InputName" placeholder="Enter Name" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div class="form-group wrapper" id="divValues1">
                    <label for="InputEmail">Parameter Name</label><span id="span1"></span>
                    <div class="input-group">
                        <input type="text" class="form-control" id="parameterName1" name="parameterName1" placeholder="Parameter Name" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                    <label for="InputEmail">Parameter Value</label>
                    <div class="input-group">
                    	<%-- <form:input path=""/> --%>
                        <input type="text" class="form-control" id="parameterValue1" name="parameterValue1" placeholder="Parameter Value" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <span>Add More Parameters</span><button id="b1" class="btn add-more" type="button">+</button>
                <div class="form-group">
                    <label for="uploadJar">Upload JAR</label>
                    <input id="uploadJar" type="file" class="file">
                </div>
                <div class="form-group">
                    <label for="sampleInput">Sample Input</label>
                    <input id="sampleInput" type="file" class="file">
                </div>
                <div class="form-group">
                    <label for="sampleOutput">Sample Output</label>
                    <input id="sampleOutput" type="file" class="file">
                </div>
               <!--  <small>Press + to add another form field :)</small> -->
                <!-- <div class="form-group">
                    <label for="InputEmail">S-Factor Base</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="InputEmailFirst" name="InputEmail" placeholder="Range 1 to 100" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="InputEmail">Iterations</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="InputEmailFirst" name="InputEmail" placeholder="Range 1 to 100" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="InputEmail">Protein Expansion Method</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="InputEmailFirst" name="InputEmail" placeholder="Range 1 to 200" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="InputEmail">Protein Interaction Source</label>
                    <div class="input-group">
                        <input type="email" class="form-control" id="InputEmailFirst" name="InputEmail" placeholder="Range 1 to 100" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="InputEmail">Disease Name</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="diseaseName" name="diseaseName" placeholder="Disease Name" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div> -->
               <!--  <div class="form-group">
                    <label for="InputEmail">Confirm Email</label>
                    <div class="input-group">
                        <input type="email" class="form-control" id="InputEmailSecond" name="InputEmail" placeholder="Confirm Email" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div> -->
                <!-- <div class="form-group">
                    <label for="InputMessage">Enter Message</label>
                    <div class="input-group">
                        <textarea name="InputMessage" id="InputMessage" class="form-control" rows="5" required></textarea>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div> -->
                <input type="submit" name="submit" id="submit" value="Request App" class="btn btn-info pull-right">
            </div>
        </form>
        <!-- <div class="col-lg-5 col-md-push-1">
            <div class="col-md-12">
                <div class="alert alert-success">
                    <strong><span class="glyphicon glyphicon-ok"></span> Success! Message sent.</strong>
                </div>
                <div class="alert alert-danger">
                    <span class="glyphicon glyphicon-remove"></span><strong> Error! Please check all page inputs.</strong>
                </div>
            </div>
        </div> -->
    </div>
</div>
</div>
            <!-- <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <h2></h2>
                        <p></p>
                        <p></p>
                    </div>
                    <div class="col-md-4">
                        <h2></h2>
                        <p></p>
                        <p></p>
                    </div>
                    <div class="col-md-4">
                        <h2></h2>
                        <p></p>
                        <p></p>
                    </div>
                </div> -->
                <hr></hr>
                
                
                <footer>
                    <p>
                    </p>
                </footer>
                
            </div>
        </body>
        <script type="text/javascript">
   		 function crunchifyAjax() {
        $.ajax({
            url : 'ajaxtest.html',
            success : function(data) {
                $('#result').html(data);
            }
        });
    }
</script>
    </html>