document.getElementById("uploadForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const formData = new FormData();
    const filesInput = document.getElementById("files");
    const jd = document.getElementById("jd").value;
    const minConfidence = document.getElementById("minConfidence").value;

    if (filesInput.files.length === 1 &&
        filesInput.files[0].name.endsWith(".zip")) {

        formData.append("file", filesInput.files[0]);

        const url = `/api/v1/process/upload-zip?minConfidence=${minConfidence}`;

        formData.append("jd", jd);

        downloadExcel(url, formData);

    } else {
        for (let file of filesInput.files) {
            formData.append("files", file);
        }

        const url = `/api/v1/process/upload?minConfidence=${minConfidence}`;
        formData.append("jd", jd);

        downloadExcel(url, formData);
    }
});

async function downloadExcel(url, formData) {
    document.getElementById("loader").classList.remove("d-none");

    const response = await fetch(url, {
        method: "POST",
        body: formData
    });

    const blob = await response.blob();
    const link = document.createElement("a");

    link.href = window.URL.createObjectURL(blob);
    link.download = "ranked_candidates.xlsx";
    link.click();

    document.getElementById("loader").classList.add("d-none");
}