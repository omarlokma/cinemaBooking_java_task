import java.util.*;

/*
 =======================================================================
  TASK: Cinema Seat Booking  -  a HashMap with your own class as the key
 =======================================================================

 A cinema hall has rows A to E, with seats 1 to 6 in each row.
 You will store the bookings in a HashMap:

     key   = Seat     (a class you finish below, for example B5)
     value = String   (the name of the customer who booked it)

 STEP 1  Complete Parts A to G in main(). Each part says what to print.
 STEP 2  Run the program BEFORE writing equals() and hashCode().
         Some results will be wrong. That is expected - write down which ones.
 STEP 3  Write equals() and hashCode() in the Seat class.
 STEP 4  Run it again and compare with the EXPECTED OUTPUT below.
 STEP 5  Answer the questions at the bottom of this file.

 RULES
   - Every time you use a seat, create a NEW object: new Seat('B', 5).
     A real program works the same way: when a customer types "B5",
     the program makes a new Seat object for it.
   - Do not change printChart().

 EXPECTED OUTPUT (after STEP 3)
 The order inside { } may be different in your output. HashMap has no order.

   Bookings:           {A1=Ali, C3=Mona, A2=Sara, B5=Omar}
   Who booked C3:      Mona
   Is A2 taken:        true
   Is E6 taken:        false
   Youssef -> B5:      refused, taken by Omar
   After Ali moved:    {C3=Mona, A2=Sara, B5=Omar, D2=Ali}
   Hana -> E1:         refused, seat is broken
   Seats booked:       4

        1  2  3  4  5  6
   A    .  X  .  .  .  .
   B    .  .  .  .  X  .
   C    .  .  X  .  .  .
   D    .  X  .  .  .  .
   E    #  #  .  .  .  .
*/

class Task_CinemaBooking {

    // A seat in the cinema hall, for example B5 = row B, seat number 5.
    // static so that main() can create Seat objects directly.
    static class Seat {
        final char row; // 'A' to 'E'
        final int number; // 1 to 6

        Seat(char row, int number) {
            this.row = row;
            this.number = number;
        }

        @Override
        public String toString() {
            return "" + row + number;
        }

        // TODO 1: override equals()
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Seat seat = (Seat) o;
            return row == seat.row && number == seat.number;
        }

        // TODO 2: override hashCode()
        @Override
        public int hashCode() {
            return Objects.hash(row, number);
        }
    }

    public static void main(String[] args) {

        // Key = Seat, Value = name of the customer who booked it
        HashMap<Seat, String> bookings = new HashMap<>();
        HashSet<Seat> broken = new HashSet<>();

        // Part A: book these seats using put()
        bookings.put(new Seat('A', 1), "Ali");
        bookings.put(new Seat('A', 2), "Sara");
        bookings.put(new Seat('B', 5), "Omar");
        bookings.put(new Seat('C', 3), "Mona");
        System.out.println("Bookings:           " + bookings);

        // Part B: who booked seat C3? Use get().
        System.out.println("Who booked C3:      " + bookings.get(new Seat('C', 3)));

        // Part C: are seats A2 and E6 taken? Use containsKey().
        System.out.println("Is A2 taken:        " + bookings.containsKey(new Seat('A', 2)));
        System.out.println("Is E6 taken:        " + bookings.containsKey(new Seat('E', 6)));

        // Part D: Youssef wants seat B5. Use putIfAbsent().
        String previous = bookings.putIfAbsent(new Seat('B', 5), "Youssef");
        if (previous == null) {
            System.out.println("Youssef -> B5:      booked");
        } else {
            System.out.println("Youssef -> B5:      refused, taken by " + previous);
        }

        // Part E: Ali moves from seat A1 to seat D2. Use remove() and put().
        String ali = bookings.remove(new Seat('A', 1));
        bookings.put(new Seat('D', 2), ali);
        System.out.println("After Ali moved:    " + bookings);

        // Part F: seats E1 and E2 are broken.
        broken.add(new Seat('E', 1));
        broken.add(new Seat('E', 2));
        // Hana wants seat E1. Book it only if it is not broken.
        if (broken.contains(new Seat('E', 1))) {
            System.out.println("Hana -> E1:         refused, seat is broken");
        } else {
            bookings.put(new Seat('E', 1), "Hana");
            System.out.println("Hana -> E1:         booked");
        }

        // Part G: how many seats are booked? Use size().
        System.out.println("Seats booked:       " + bookings.size());

        // GIVEN - prints the seating chart
        System.out.println();
        printChart(bookings, broken);
    }

    // GIVEN - do not change. Prints the hall: X = booked # = broken . = free
    static void printChart(HashMap<Seat, String> bookings, HashSet<Seat> broken) {
        System.out.println("     1  2  3  4  5  6");
        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print(row + "  ");
            for (int number = 1; number <= 6; number++) {
                Seat seat = new Seat(row, number);
                if (bookings.containsKey(seat)) {
                    System.out.print("  X");
                } else if (broken.contains(seat)) {
                    System.out.print("  #");
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }
    }
}

/*
 * QUESTIONS - write your answers here
 * 
 * Q1.Why did we have to override hashcode() and equals()?
 * By default Java compares memory addresses instead of values. Since we create a new Seat(...) every time, 
 * Java thinks two seats with the same row and number are completely different objects. Overriding both tells 
 * HashMap and HashSet to put them in the same bucket (hashCode) and treat them as equal (equals) whenever row 
 * and number match.
 * ------------------------------------------------------------------------------------------------------------
 * Q2. Delete your hashCode() but keep equals(), and run again.
 * What happens? Why?
 * produces the same wrong results as if equals() wasn't there (C3 returns null, A2 returns false, B5 gets booked 
 * twice, D2=null gets added making the total 7, E1 gets booked even though it's broken, and the chart stays all dots).
 * HashMap and HashSet check hashCode() first to find the right bucket without it, every new Seat gets a different 
 * hash code from its memory address, so they look in an empty bucket and never even reach equals().
 * -------------------------------------------------------------------------------------------------------------
 * Q3. Why are row and number final? What could go wrong if the number
 * of a booked seat could be changed after it was put in the map?
 * They are final so a Seat key cannot be modified after it's created. If you added B5 to the map and then changed 
 * its number to 6, its hashCode() would change, but the map wouldn't move it to the new bucket. The entry would get
 * lost searching for B6 checks the wrong bucket, and searching for B5 finds the old bucket where equals() fails because the number is now 6.
 * ---------------------------------------------------------------------------------------------------------------
 * Q4. Change equals(Object o) to equals(Seat o). What happens?
 * Then also remove @Override and run again. What happens now?
 * With @Override, the code won't compile because equals(Seat) doesn't match Object.equals(Object). After removing 
 * @Override, it compiles and runs, but gives wrong results again. Changing the parameter type overloads the method 
 * instead of overriding it, so HashMap ignores it and falls back to Object.equals(Object), which compares memory addresses.
 * -----------------------------------------------------------------------------------------------------------------
 * BONUS. Replace the whole Seat class with this one line:
 * record Seat(char row, int number) {}
 * Run it. What happens, and why?
 * Everything works properly bookings, lookups, and the seating chart all match the expected output
 * (the only difference is that keys print as Seat[row=A, number=1] instead of A1). A Java record automatically 
 * makes row and number final and generates the constructor, equals(), hashCode(), and toString() 
 * behind the scenes using both fields.
 * ----------------------------------------------------------------------------------------------------------------
 */
