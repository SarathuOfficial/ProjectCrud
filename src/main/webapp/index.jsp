<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>CRUD Operations</title>
    <style>
       body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f4f4;
}

.container {
    width: 80%;
    margin: 20px auto;
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    text-align: center; /* Center text inside container */
}

h1 {
    color: #333;
}

button {
    background-color: #007bff;
    color: #fff;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    margin: 5px;
}

button:hover {
    background-color: #0056b3;
}

.tab-content {
    display: none;
}

.tab-content.active {
    display: block;
}

form {
    display: grid;
    gap: 15px;
    max-width: 600px;
    margin: 0 auto;
    text-align: left; /* Align text inside form */
}

label {
    font-weight: bold;
    color: #333;
}

input[type="text"],
input[type="date"],
input[type="number"],
textarea,
select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
}

textarea {
    resize: vertical;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
}

table th, table td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
}

table th {
    background-color: #f2f2f2;
    color: #333;
}

table tr:nth-child(even) {
    background-color: #f9f9f9;
}

img {
    max-width: 100px;
    max-height: 100px;
}

.tab-content h2 {
    margin-top: 0;
}

.form-group {
    display: flex;
    flex-direction: column;
}

.message {
    color: green;
    font-weight: bold;
}

.error-message {
    color: red;
    font-weight: bold;
}

    </style>

    <script>
    function showTab(tabId) {
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
        document.getElementById(tabId).classList.add('active');
    }

    function sendDeleteRequest() {
        const projectId = document.getElementById('deleteProjectId').value;
        fetch('DeleteProject', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'text/plain',
            },
            body: projectId
        })
        .then(response => response.text())
        .then(data => {
            document.getElementById('deleteResult').innerText = data;
        })
        .catch(error => {
            document.getElementById('deleteResult').innerText = 'Error: ' + error.message;
        });
    }
    
    async function loadProjects() {
        try {
            const response = await fetch('ReadProject');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();

            // Find the table body element
            const tableBody = document.querySelector('#projectsTable tbody');
            
            // Check if tableBody exists
            if (!tableBody) {
                throw new Error('Table body element not found');
            }

            tableBody.innerHTML = ''; // Clear existing rows

            data.forEach(project => {


                const row = document.createElement('tr');

                const projectIdCell = document.createElement('td');
                projectIdCell.textContent = project.id || 'N/A';
                row.appendChild(projectIdCell);

                const projectNameCell = document.createElement('td');
                projectNameCell.textContent = project.name || 'N/A';
                row.appendChild(projectNameCell);

                const projectDescriptionCell = document.createElement('td');
                projectDescriptionCell.textContent = project.description || 'N/A';
                row.appendChild(projectDescriptionCell);

                const projectStartDateCell = document.createElement('td');
                projectStartDateCell.textContent = project.startDate || 'N/A';
                row.appendChild(projectStartDateCell);

                const projectEndDateCell = document.createElement('td');
                projectEndDateCell.textContent = project.endDate || 'N/A';
                row.appendChild(projectEndDateCell);

                const projectBudgetCell = document.createElement('td');
                projectBudgetCell.textContent = project.budget || 'N/A';
                row.appendChild(projectBudgetCell);

                const projectImageCell = document.createElement('td');
                if (project.image) {
                    const img = document.createElement('img');
                    img.src = project.image;
                    img.alt = "Project Image";
                    img.style.maxWidth = '100px'; // Set max-width for images
                    img.style.maxHeight = '100px'; // Set max-height for images
                    projectImageCell.appendChild(img);
                } else {
                    projectImageCell.textContent = 'No Image';
                }
                row.appendChild(projectImageCell);

                tableBody.appendChild(row);
            });

            showTab('read'); // Show the "Read Projects" tab after data is loaded
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    
    
    function createProject() {
        const form = document.getElementById('createForm');
        const formData = new FormData(form);

        fetch('CreateProject', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            document.getElementById('createResult').innerText = data;
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('createResult').innerText = 'Error: ' + error.message;
        });
    }


    function updateProject() {
        const form = document.getElementById('updateForm');
        const formData = new FormData(form);

        fetch('UpdateProject', {
            method: 'PUT',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            document.getElementById('updateResult').innerText = data;
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('updateResult').innerText = 'Error: ' + error.message;
        });
    }

</script>
</head>
<body>
    <div class="container">
        <h1>CRUD Operations</h1>
        <button onclick="showTab('create')">Create Project</button>
        <button onclick="loadProjects()">Read Projects</button>
        <button onclick="showTab('update')">Update Project</button>
        <button onclick="showTab('delete')">Delete Project</button>

        <div id="create" class="tab-content">
            <h2>Create Project</h2>
			<form id="createForm" action="CreateProject" method="post" enctype="multipart/form-data" onsubmit="event.preventDefault(); createProject();">
                <label for="createProjectName">Project Name</label>
                <input type="text" id="createProjectName" name="projectName" required>

                <label for="createProjectDescription">Project Description</label>
                <textarea id="createProjectDescription" name="projectDescription" rows="4" required></textarea>

                <label for="createStartDate">Start Date</label>
                <input type="date" id="createStartDate" name="startDate" required>

                <label for="createEndDate">End Date</label>
                <input type="date" id="createEndDate" name="endDate" required>

                <label for="createStatus">Status</label>
                <select id="createStatus" name="status" required>
                    <option value="Not Started">Not Started</option>
                    <option value="In Progress">In Progress</option>
                    <option value="Completed">Completed</option>
                </select>

                <label for="createBudget">Budget</label>
                <input type="number" step="0.01" id="createBudget" name="budget" required>

                <label for="createImage">Project Image</label>
                <input type="file" id="createImage" name="image">

                <button type="submit">Create Project</button>
                <p id="createResult" class="message"></p>
            </form>
        </div>

        <div id="read" class="tab-content">
            <h2>Project List</h2>
            <table id="projectsTable">
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
                    <!-- Data will be filled by JavaScript -->
                </tbody>
            </table>
        </div>
        
                <div id="update" class="tab-content">
            <h2>Update Project</h2>
			<form id="updateForm" action="UpdateProject" method="post" enctype="multipart/form-data" onsubmit="event.preventDefault(); updateProject();">
                <label for="updateProjectId">Project ID</label>
                <input type="text" id="updateProjectId" name="projectId" required>

                <label for="updateProjectName">Project Name</label>
                <input type="text" id="updateProjectName" name="projectName">

                <label for="updateProjectDescription">Project Description</label>
                <textarea id="updateProjectDescription" name="projectDescription" rows="4"></textarea>

                <label for="updateStartDate">Start Date</label>
                <input type="date" id="updateStartDate" name="startDate">

                <label for="updateEndDate">End Date</label>
                <input type="date" id="updateEndDate" name="endDate">

                <label for="updateStatus">Status</label>
                <select id="updateStatus" name="status">
                    <option value="Not Started">Not Started</option>
                    <option value="In Progress">In Progress</option>
                    <option value="Completed">Completed</option>
                </select>

                <label for="updateBudget">Budget</label>
                <input type="number" step="0.01" id="updateBudget" name="budget">

                <label for="updateImage">Project Image</label>
                <input type="file" id="updateImage" name="image">

                <button type="submit">Update Project</button>
                <p id="updateResult" class="message"></p>
            </form>
        </div>

        <div id="delete" class="tab-content">
            <h2>Delete Project</h2>
            <form id="deleteForm" action="DeleteProject" method="post" onsubmit="event.preventDefault(); sendDeleteRequest();">
                <label for="deleteProjectId">Project ID</label>
                <input type="text" id="deleteProjectId" name="projectId" required>

                <button type="submit">Delete Project</button>
                <p id="deleteResult" class="message"></p>
            </form>
        </div>
    </div>
</body>
</html>
        