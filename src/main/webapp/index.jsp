<!DOCTYPE html>
<html>
<head>
    <title>CRUD Operations</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #f0f0f0;
        }
        .container {
            margin-top: 100px;
        }
        button {
            margin: 10px;
            padding: 15px 30px;
            font-size: 16px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }
        
    </style>
</head>
<body>
    <div class="container">
        <h1>CRUD Operations</h1>
        <button onclick="window.location.href='CreateProject.jsp'">Create</button>
		<button onclick="window.location.href='/ProjectCRUD/ReadProject'">Read</button>
        <button onclick="window.location.href='UpdateProject.jsp'">Update</button>
        <button onclick="window.location.href='DeleteProject.jsp'">Delete</button>
    </div>
</body>
</html>
