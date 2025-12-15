async function loadDocuments() {
    const response = await fetch("/api/documents");
    const docs = await response.json();

    const list = document.getElementById("docList");
    list.innerHTML = "";

    if (docs.length === 0) {
        list.innerHTML = "<p class='text-muted'>No documents found.</p>";
        return;
    }

    docs.forEach(d => {
        const div = document.createElement("div");
        div.className = "card mb-2 shadow-sm";

        div.innerHTML = `
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <h5 class="card-title mb-1">
                        <i class="bi bi-file-earmark-pdf"></i> ${d.fileName}
                    </h5>
                
                    <p class="card-text text-muted mb-1">
                        Type: ${d.mimeType}<br>
                        Uploaded: ${new Date(d.uploadDate).toLocaleString()}
                    </p>
                
                    <p class="card-text mt-2">
                        <strong>Summary:</strong><br>
                        ${d.summary ? d.summary : "<span class='text-muted'>Summary is not available yet...</span>"}
                    </p>
                </div>
            </div>
        `;

        list.appendChild(div);
    });
}

async function uploadPDF(event) {
    event.preventDefault();

    const fileInput = document.getElementById("pdfFile");
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a PDF file");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    const response = await fetch("/api/documents/upload", {
        method: "POST",
        body: formData
    });

    if (response.ok) {
        alert("PDF uploaded!");
        loadDocuments();
    } else {
        alert("Upload failed");
    }
}
