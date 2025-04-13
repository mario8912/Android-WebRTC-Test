using System;
using Microsoft.AspNetCore.SignalR;
using WebCamServer3.Utils;

namespace WebCamServer3.Hubs;
public class WebRtcHub : Hub
{
    private readonly static Dictionary<string, string> offers = [];
    public override async Task OnConnectedAsync()
    {
        offers.Add(Context.ConnectionId, "");

        Console.WriteLine($"Cliente conectado: {Context.ConnectionId}");
        await Clients.Caller.SendAsync("ReceiveMessage", "Servidor", "¡Te has conectado correctamente al WebRTC Hub!");
        await base.OnConnectedAsync();
    }

    public override async Task OnDisconnectedAsync(Exception? exception)
    {
        Console.WriteLine($"Cliente desconectado: {Context.ConnectionId}");
        await base.OnDisconnectedAsync(exception);
    }

    public async Task SendOffer(string offer)
    {
        offers[Context.ConnectionId] = offer;
        await Clients.Others.SendAsync("ReceiveOffer", offer);

        using Logs log = new(offer);
        await log.WrtieAndSaveLog();
    }

    public async Task SendAnswer(string remoteSdp)
    {
        await Clients.Others.SendAsync("ReceiveAnswer", remoteSdp);
    }

    public async Task SendIceCandidate(string iceCandidate)
    {
        Console.WriteLine(iceCandidate);
        await Clients.Others.SendAsync("ReceiveIce", iceCandidate);
    }
}
