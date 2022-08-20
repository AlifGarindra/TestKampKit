core sdk features

1. sdk authentication with clientKey + phone device id + application id
    1. session for sdk
2. available menu/product, array of:
    1. id
    2. name = "Jual/Beli Emas", "Pulsa Telepon"
    3. code_product = "emas", "pulsa"
    4. description
    5. webpage link
    6. image icon
    7. color icon (hex color codes)
3. master data
    1. partner name
    2. theme props
        1. partner logo/icon = pixel size 96px
        2. partner theme color (hex color codes)
        3. header
        4. background
        5. accent
    3. priority:
        1. theme dari cms, di cms ada radio button untuk penanda activate theme yang dari cms atau tidak
        2. codingan
        3. default theme
4. Widget
    1. Super App Menu
    2. Full Screen Super App menu
    3. Smart Component (case: saldo live update, order list(history) live update)
    4. Native Screen Page per feature instead webview
    5. Android/iOS Home Screen Widget (related with the products)
5. UI Customization
    1. Sortable Menu
        1. Bisa manageable via CMS
        2. pre-define (contoh: AscById, ByRank, ByName, dll)
        3. custom (pass a function/method untuk yang body/isi code nya di coding partner dev), partner dev cukup mengetahui apa yang bisa di custom/override, sedangkan untuk meng apply sort nya pada View sudah SDK yang handle (Declarative)
        view.applySOrt(Sort.AscById)

        view.setOnLoadData(new LoadDataListener(List<Menu> menu){
          return menu
        })

        view.customSort(items => {
          // TODO: partner code
          var newItems = [];
          var emas = items.getEmas()
          newItems.push(emas)
          newItems.psuh(rest)

          return newItems
        })
    2. Menu Layout option
        1. Grid
            1. adjustable row and column
                1. View akan wrap berdasarkan jumlah Row
                ______
                |ooooo|oooo
                |ooooo|
                |ooooo
                |ooooo
                |ooooo
                2. Column lebih dari 4 akan otomatis scollable horizontal
            2. ordering mode (render per item dari kiri ke kanan atau atas ke bawah) *low prior
            3. Maximum Item
                1. Last Item adalah ‘show more’ button ( lanjut ke Full Screen)
        2. Slider
                ______
                |ooooo|oooo
            1. Layout
                1. Horizontal
                2. Vertical
            2. Mode View
                1. Card View
                2. Small Card View
                3. Mini Slider (Text Only item)
    3. Menu as Single Item/Button
      1. ui si partner, onclik pakai OttoSDK.openEmas()
      BUTTON.setOnClickListener(new ocl() {
        OttoSDK.getProduct().openEmas()
      })
      2. UI Button dari SDK include logic

    4. Menu on Full Screen
    5. UI Customization Attribute
        1. Theme Color
            1. Light and Dark Theme
                1. Extendable Attribute
                2. Beberapa predefined theme (contoh seperti color theme pada code editor)
        2. Menu Item
            1. Border (color, width, etc)
            2. Background Color
            3. Content position alignment
            4. Margin and Padding
            5. Font (face, style, color, size
            6. onPress ui feedback
6. Event Listener (callback function)
    1. Listen every of UI action (page navigation, button click, submit api)
        1. For every button need documented its ID and TYPE
    2. Give an option for code override every UI Action, contoh:
        1. Partner Dev overriding ‘submit action’ from SDK UI/Page to continue with SDK API Wrapper 
            1. usecase nya: Dari Summary Page (SDK) dan user do action Checkout disitu, bisa dilanjutkan ke Page di luar dari SDK, misalnya ThankYou Page pakai yang Partner drpd SDK punya.
7. SDK as API Wrapper (like Midtrans CoreKit)
8. Accept Firebase Analytic Session tracker ID, SDK will handle all supported tracked action.
    1. Analytic Servic Support:
        1. Firebase Analytic/Google Analytic
9. Add bugtracker sentry.io
10. 
