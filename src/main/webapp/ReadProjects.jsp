<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Read Projects</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
        }
        .container {
            margin: 100px auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            max-width: 1000px;
            width: 100%;
        }
        h1 {
            margin: 0 0 20px;
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
        }
        th {
            background-color: #f2f2f2;
            color: #333;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Project List</h1>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Budget</th>
                    <th>Image</th>
                </tr>
            </thead>
            <tbody>
                <% 
                List<String[]> projects = (List<String[]>) request.getAttribute("projects");
                
                if (projects != null && !projects.isEmpty()) {
                    for (String[] project : projects) { 
                %>
                <tr>
                    <td><%= project[0] %></td>
                    <td><%= project[1] %></td>
                    <td><%= project[2] %></td>
                    <td><%= project[3] %></td>
                    <td><%= project[4] %></td>
                    <td><%= project[5] %></td>
                    <td>
                        <% if (project[6] != null) { %>
                            <img src="<%= project[6] %>" alt="Project Image" width="100" height="100"/>
                        <% } else { %>
                            No Image
                        <% } %>
                    </td>
                </tr>
                <% 
                    }
                } else {
                %>
                <tr>
                    <td colspan="7">No projects available</td>
                </tr>
                <% 
                }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>