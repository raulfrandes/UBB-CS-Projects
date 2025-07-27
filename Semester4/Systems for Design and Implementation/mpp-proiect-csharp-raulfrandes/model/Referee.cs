using System;
using Newtonsoft.Json;

namespace model;

[Serializable]
public class Referee(string name, string username, string password) : Entity<long>
{
    [JsonProperty("name")]
    public string Name { get; set; } = name;
    [JsonProperty("username")]
    public string Username { get; set; } = username;
    [JsonProperty("password")]
    public string Password { get; set; } = password;

    public override string ToString()
    {
        return base.ToString() + $", Name: {Name}, Username: {Username}, Password: {Password}";
    }
}