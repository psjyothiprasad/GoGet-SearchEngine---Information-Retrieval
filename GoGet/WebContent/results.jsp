<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Go-Get</title>
<style>
			body{
				background-color: #FFFFFF;
			}
			/* #querybox {
				width: 700px;
				height: 40px;
			} */
			#goget {
				font-size: 75px;
				font-family: cursive;
				color: tomato;
			}
			#goget_results {				
				font-family: cursive;
				color: tomato;
				font-size: 34px;
				position: absolute;
				top: 10px;
			}
			.scroll{
			}
			#querybox {
				width: 700px;
				height: 40px;
				position: absolute;
				left: 160px;
				top: 15px;
			}
			li {
				font-family: cursive;
				color: tomato;
				height: 80px;
				border-bottom: 1px solid white;
				width: 1860px;
			}
			
		</style>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		<script type="text/javascript">
		$(document).ready(function () {
			
			//$("#results_form").submit(function () {
			$("#feedback_button").click(function () {
				
				$("#results_form").attr('action','FeedbackController');
				$("#results_form").submit();
				alert($("#results_form").attr('action'));
				 var notChecked = [], checked = [];
				 $(":checkbox").each(function() {
					 
								 
			            if(this.checked){
			                checked.push(this.id);
			            } else {
			                notChecked.push(this.id);
			            }
			        });
					 
				 alert(checked);
				 
				 
				 
				 
				 
				 $.post("FeedbackController",
						 {
					 		relevant_files: checked
						 });					 
				});
		});
		</script>
</head>
<body>
<form  id = "results_form" action="GoGetterController" method="POST">
<div id = "results_page"  style="">
		<table height="100%" width="100%">
			<tbody>
				<tr>
					<td>
						<label id = "goget_results" name ="goget" >Go - Get</label>
						<input type = "text" id = "querybox" name ="query" >
						<input type="image" src="search-12-xxl.png" border="0" style="height: 40px;  position: absolute; left: 867px; top: 15px;" alt="Submit" />
						<button type="button" id="feedback_button" style="height: 40px; width: 350px; position: absolute; left: 950px; top: 15px;" alt ="Submit">Submit Feedback</button>
						<!-- <img src = "C:\Users\sande\workspace\GoGet\WebContent\Images\search-12-xxl.png" id = "search_icon_results" style="height: 40px;  position: fixed; left: 867px; top: 15px;"/> -->
					</td>
				</tr>
				<tr style="position: absolute; top: 85px;">
					<td>
						<% 
						List<String> rankedlist = new ArrayList<String>();
						rankedlist.addAll((List<String>)session.getAttribute("rankedlist"));
						Map<String,Double> similarities = (Map<String,Double>)session.getAttribute("similarities");
						Map<String,Double> proximities = (Map<String,Double>)session.getAttribute("proximities");%>
						
						
						
					
						<div class="scroll">
						<ul id="results">
							
							<% 	for(int i = rankedlist.size()-1;i>=0;i--){	
					%>	
							<li><input type="checkbox" name="relevance_box" id="<%=rankedlist.get(i)%>" value="<%=rankedlist.get(i)%>" class="relevance_box"></input><a href="file:///E:/Sandeep/docsnew/<%=rankedlist.get(i)%>"> <%=rankedlist.get(i)%><br>file:///E:/Sandeep/docsnew/<%=rankedlist.get(i)%></a><div><%=proximities.get(rankedlist.get(i)) %></div></li>		
					<%}%>
					
						</ul>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</form>
</body>
</html>