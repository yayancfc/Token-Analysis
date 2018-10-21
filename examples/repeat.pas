program repeatProgram;
var x: integer;

begin
    x := 0;
    writeln(x);
    repeat
        x := x + 1 * 2;

    until x >= 20;
    writeln(x);
end.