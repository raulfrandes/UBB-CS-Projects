namespace model;

public class Result(Participant participant, Trial trial, int points) : Entity<long>
{
        public Participant Participant { get; set; } = participant;
        public Trial Trial { get; set; } = trial;
        public int Points { get; set; } = points;

        public override string ToString()
        {
                return base.ToString() + $", Participant: {Participant}, Trial: {Trial}, Points: {Points}";
        }
}