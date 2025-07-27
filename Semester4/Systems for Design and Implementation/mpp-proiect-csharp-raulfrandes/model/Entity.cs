using System;
using Newtonsoft.Json;

namespace model;

[Serializable]
public class Entity<T>
{
    [JsonProperty("id")]
    public T Id { get; set; }
    
    public override string ToString()
    {
        return $"Id: {Id}";
    }
}