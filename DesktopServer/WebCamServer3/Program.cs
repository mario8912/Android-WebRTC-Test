using WebCamServer3.Hubs;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowSpecificOrigin", policy =>
        policy.WithOrigins("http://localhost:3000", "http://192.168.1.136:3000") // Cambia esto por la URL de tu frontend
              .AllowAnyMethod()
              .AllowAnyHeader()
              .AllowCredentials()); // 
});


// Agregar SignalR al contenedor de dependencias
builder.Services.AddSignalR();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();
}

app.UseCors("AllowSpecificOrigin");
app.MapHub<WebRtcHub>("/webrtchub");

app.UseRouting();

app.Run();
