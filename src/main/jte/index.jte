@param String name

@template.layout.main(title = "Trang chủ", content= @`
    <div class="card bg-base-100 shadow-xl">
        <div class="card-body">
            <h2 class="card-title text-display">Chào mừng, ${name}!</h2>
            <p>Đây là nội dung trang con sử dụng font Inter mặc định.</p>
            <div class="card-actions justify-end">
                <button class="btn btn-primary">OK</button>
            </div>
        </div>
    </div>
`)
