using System;
using System.Runtime.CompilerServices;

namespace WebCamServer3.Utils
{
    internal class Logs : IDisposable
    {
        private readonly string _offer;
        private StreamWriter? _writer;
        public Logs(string offer)
        {
            _offer = offer;
            InitializeStreamWriter();
        }
        public async Task WrtieAndSaveLog()
        {
            if (_writer != null)
                await _writer.WriteLineAsync(_offer);
        }

        private void InitializeStreamWriter()
        {
            string path = GetPath();
            _writer = new StreamWriter(path);
        }

        private static string GetPath()
        {
            string projectDir = AppDomain.CurrentDomain.BaseDirectory;
            DateTime moment = DateTime.Now;
            string fileName = string.Format("offert{0}_{1}_{2}_{3}.txt", moment.Day, moment.Month, moment.Year, moment.Second);
            
            return Path.Combine(projectDir, fileName);
        }

        void IDisposable.Dispose()
        {
            _writer?.Close();
            _writer?.Dispose();
        }

        ~Logs()
        {
            ((IDisposable)this).Dispose();
        }
    }
}
