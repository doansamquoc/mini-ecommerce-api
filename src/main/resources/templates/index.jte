@param String title

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>${title}</title>
    <link rel="stylesheet" href="/css/output.css">
</head>
<body class="bg-base-200">
<div class="navbar bg-primary text-primary-content">
    <a class="btn btn-ghost text-xl">My App</a>
</div>

<main class="p-10">
    <h1 class="text-3xl font-bold">${title}</h1>
    <p class="mt-4">Đây là nội dung được Render bởi JTE cực nhanh!</p>

    <div class="mt-6">
        <a href="/swagger-ui.html" class="btn btn-secondary">Xem API Doc (Swagger)</a>
    </div>
</main>
</body>
</html>
