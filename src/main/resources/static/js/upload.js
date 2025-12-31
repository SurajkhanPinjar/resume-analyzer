document.addEventListener("DOMContentLoaded", () => {

    console.log("✅ upload.js loaded");

    const form = document.getElementById("uploadForm");

    if (!form) {
        console.error("❌ uploadForm not found");
        return;
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const filesInput = document.getElementById("files");
        const minConfidenceInput =
            document.getElementById("minConfidence")?.value;

        const minConfidence =
            minConfidenceInput && minConfidenceInput !== ""
                ? minConfidenceInput
                : "0.0";

        if (!filesInput || filesInput.files.length === 0) {
            alert("Please select resume files");
            return;
        }

        const formData = new FormData();

        // ================= JD HANDLING =================
        const jdType =
            document.querySelector('input[name="jdType"]:checked')?.value;

        if (!jdType) {
            alert("Please select Job Description type");
            return;
        }

        // ✅ TEXT JD
        if (jdType === "text") {
            const jdText = document.getElementById("jdText")?.value;

            if (!jdText || jdText.trim() === "") {
                alert("Please paste Job Description text");
                return;
            }

            formData.append("jd", jdText);
        }

        // ✅ FILE JD
        else if (jdType === "file") {
            const jdFile = document.getElementById("jdFile")?.files[0];

            if (!jdFile) {
                alert("Please upload JD TXT file");
                return;
            }

            formData.append("jdFile", jdFile);
        }

        // ================= ZIP FLOW =================
        if (
            filesInput.files.length === 1 &&
            filesInput.files[0].name.toLowerCase().endsWith(".zip")
        ) {
            formData.append("file", filesInput.files[0]);

            const url =
                `/api/v1/process/upload-zip?minConfidence=${minConfidence}`;

            await downloadExcel(url, formData);
        }

        // ================= PDF FLOW =================
        else {
            for (let file of filesInput.files) {
                if (!file.name.toLowerCase().endsWith(".pdf")) {
                    alert("Only PDF files are allowed");
                    return;
                }
                formData.append("files", file);
            }

            const url =
                `/api/v1/process/upload?minConfidence=${minConfidence}`;

            await downloadExcel(url, formData);
        }
    });
});


// ================= DOWNLOAD HANDLER =================
async function downloadExcel(url, formData) {

    const loader = document.getElementById("loader");
    loader?.classList.remove("d-none");

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "X-API-KEY": "abcd-123"
            },
            body: formData
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "Upload failed");
        }

        const blob = await response.blob();

        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = "ranked_candidates.xlsx";
        document.body.appendChild(link);
        link.click();
        link.remove();

    } catch (err) {
        console.error("❌ Upload failed:", err);
        alert("Failed to process resumes");
    } finally {
        loader?.classList.add("d-none");
    }
}