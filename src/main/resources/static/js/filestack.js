const client = filestack.init(filestackKey);
const options = {
    fromSources: ["local_file_system"],
    accept: ["image/*"],
    onFileUploadFinished: callback =>{
        const filestackUrl = callback.url;
        $('#photo').val(filestackUrl);
    }
};
$('#upload').click(function (event){
    event.preventDefault();
    client.picker(options).open();
})