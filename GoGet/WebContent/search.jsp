
<html>
	<head>
		<style>
			body{
				background-color: slategrey;
			}
			#querybox {
				width: 700px;
				height: 40px;
			}
			#goget {
				font-size: 75px;
				font-family: cursive;
				color: tomato;
			}
			#goget_results {				
				font-family: cursive;
				color: tomato;
				font-size: 34px;
				position: fixed;
				top: 10px;
			}
			#querybox_results {
				width: 700px;
				height: 40px;
				position: fixed;
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
		<script>
			<%-- $(document).ready(function(){
				$("#search_icon_search, #search_icon_results").submit(function(){
					<% 
						List<String> rankedlist = new ArrayList<String>();
						rankedlist.addAll((List<String>)session.getAttribute("rankedlist"));
						for(int i = rankedlist.size()-1;i>=0;i--){	
					%>
						var listelement = "<li><div><label>"+<%rankedlist.get(i); %>+"</label><br><label>https://www.google.com</label></div></li>"
						$(listelement).appendTo("#results");
					<%}%>
					$("#search_page").hide();
					$("#results_page").show();
					
				});
			}); --%> 
		</script>
	</head>
	<body id ="body">
	<form name ="goget_form" action="GoGetterController" method="POST">
	<div id = "search_page" >
		<table height="100%" width="100%">
			<tbody>
				<tr height="30%"><td></td></tr>
				<tr height="10%">
					<td align="center">
						<label id = "goget" name ="goget" >Go - Get</label>
					</td>
				</tr>
				<tr height="10%">
					<td align="center">
					<table>
					<tr>
					<td>
						<input type = "text" id = "querybox" name ="query" >
					</td>
					<td>
						<input type="image" src="search-12-xxl.png" id = "search_icon_search" border="0" style="height: 40px;   left: 1305px; top: 435px;" alt="Submit" />
						<!-- <img src = "C:\Users\sande\workspace\GoGet\WebContent\Images\search-12-xxl.png" id = "search_icon_search" style="height: 40px; margin-top: 0px;"/> -->
					</td>
					</tr>
					</table>
				</tr>
				<tr height="50%"><td></td></tr>
			</tbody>
		</table>
	</div>
	</form>
	</body>

</html>