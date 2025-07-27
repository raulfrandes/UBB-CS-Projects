using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Runtime.InteropServices;
using System.Threading;
using System.Threading.Tasks;
using model;

namespace RestClient
{
    internal class Program
    {
        static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

        private static string URL_Base = "http://localhost:8080/triathlon/trials";
        
        public static void Main(string[] args)
        {
            RunAsync().Wait();
        }

        static async Task RunAsync()
        {
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            
            // Save a trial
            Referee referee = new Referee("Alex Johnson", "alex", "alex");
            referee.Id = 1L;
            Trial trial = new Trial("A trial for testing", "Test trial", referee);
            Console.WriteLine("Save trial {0}", trial);
            Trial resultSave = await SaveTrialAsync("http://localhost:8080/triathlon/trials", trial);
            Console.WriteLine("Am primit {0}", resultSave);
            Console.ReadLine();
            
            // Find all trials
            Console.WriteLine("Find all trials");
            Trial[] resultFindAll = await FindAllAsync("http://localhost:8080/triathlon/trials");
            Console.WriteLine("Am primit:");
            trial.Id = resultFindAll[resultFindAll.Length - 1].Id;
            
            foreach (Trial t in resultFindAll)
            {
                Console.WriteLine(t);
            }
            Console.WriteLine();
            
            // Find Trial by Id
            Console.WriteLine("Find trial with id {0}", 1);
            Trial resultFindById = await FindByIdAsync("http://localhost:8080/triathlon/trials/1");
            Console.WriteLine("Am primit {0}", resultFindById);
            Console.WriteLine();
            
            // Update trial
            trial.Name = "Updated test trial";
            Console.WriteLine("Update trial {0}", trial.Id);
            Trial resultUpdate = await UpdateAsync("http://localhost:8080/triathlon/trials", trial);
            Console.WriteLine("Am primit {0}", resultUpdate);
            Console.WriteLine();
            
            // Delete trial
            Console.WriteLine("Delete trial {0}", trial.Id);
            DeleteAsync("http://localhost:8080/triathlon/trials/" + trial.Id);
            Console.WriteLine();
        }

        static async Task<Trial> SaveTrialAsync(String path, Trial trial)
        {
            Trial result = null;
            HttpResponseMessage response = await client.PostAsJsonAsync(path, trial);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<Trial>();
            }

            return result;
        }

        static async Task<Trial[]> FindAllAsync(String path)
        {
            Trial[] trials = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                trials = await response.Content.ReadAsAsync<Trial[]>();
            }

            return trials;
        }

        static async Task<Trial> FindByIdAsync(String path)
        {
            Trial result = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<Trial>();
            }

            return result;
        }

        static async Task<Trial> UpdateAsync(String path, Trial trial)
        {
            Trial result = null;
            HttpResponseMessage response = await client.PutAsJsonAsync(path, trial);
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<Trial>();
            }

            return result;
        }

        static async void DeleteAsync(String path)
        {
            await client.DeleteAsync(path);
        }
    }

    public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler) : base(innerHandler)
        {
        }

        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,
            CancellationToken cancellationToken)
        {
            Console.WriteLine("Request:");
            Console.WriteLine(request.ToString());
            if (request.Content != null)
            {
                Console.WriteLine(await request.Content.ReadAsStringAsync());
            }
            Console.WriteLine();

            HttpResponseMessage response = await base.SendAsync(request, cancellationToken);
            
            Console.WriteLine("Response:");
            Console.WriteLine(response.ToString());
            if (response.Content != null)
            {
                Console.WriteLine(await response.Content.ReadAsStringAsync());
            }
            Console.WriteLine();

            return response;
        }
    }
}