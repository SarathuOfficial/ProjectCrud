<!DOCTYPE html>
<html>
<head>
    <title>Update Project</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 500px;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        label {
            display: block;
            margin-top: 20px;
            color: #555;
        }
        input, textarea, select {
            width: calc(100% - 20px);
            padding: 10px;
            margin-top: 5px;
            border-radius: 5px;
            border: 1px solid #ccc;
            font-size: 16px;
            color: #333;
        }
        button {
            display: block;
            width: 100%;
            padding: 15px;
            margin-top: 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Update Project</h1>
        <form action="UpdateProject" method="post" enctype="multipart/form-data">
            <label for="projectId">Project ID</label>
            <input type="text" id="projectId" name="projectId" required>
            
            <label for="projectName">Project Name</label>
            <input type="text" id="projectName" name="projectName" required>
            
            <label for="projectDescription">Project Description</label>
            <textarea id="projectDescription" name="projectDescription" rows="4" required></textarea>
            
            <label for="startDate">Start Date</label>
            <input type="date" id="startDate" name="startDate" required>
            
            <label for="endDate">End Date</label>
            <input type="date" id="endDate" name="endDate" required>
            
            <label for="status">Status</label>
            <select id="status" name="status" required>
                <option value="Not Started">Not Started</option>
                <option value="In Progress">In Progress</option>
                <option value="Completed">Completed</option>
            </select>
            
            <label for="budget">Budget</label>
            <input type="number" step="0.01" id="budget" name="budget" required>

            <label for="image">Project Image</label>
            <input type="file" id="image" name="image">
            
            <button type="submit">Update Project</button>
        </form>
    </div>
</body>
</html>