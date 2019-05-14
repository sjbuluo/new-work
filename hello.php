<?php
/**
 * Created by IntelliJ IDEA.
 * User: 华硕
 * Date: 2019-05-14
 * Time: 18:19
 */
echo("Hello World!\n");
$boo = true;
if($boo == true) {
    echo("真\n");
} else {
    echo("假\n");
}
$string = <<< str
这是第一行
这是第二行
这是第三行
这是最后一行
str;
echo($string);
$ba = 0123;
echo($ba);
echo("\n");
echo($ba==83);
echo("\n");
echo(0x123);
echo("\n");
echo(123);
echo("\n");
echo(3.1415);
echo("\n");
echo(31415E-4);
echo("\n");
echo(pi());
echo("\n");
$array = array('A', 'b', 'C');
//echo($array);
echo("\n");
$array['name'] = 'SJ';
$array['age'] = 25;
$array['gender'] = 'man';
//echo($array);
echo("\n");
echo __FILE__;
echo __LINE__;
$param1 = '123';
$param2 = & $param1;
$param1 = '321';
echo $param2;
echo '<br />';
echo '123'.'321'.'<br />';
echo null || 'ABC';
echo '<br />';
$name = 'SJ';
function changeName(& $n) {
    $n = 'JS';
}
changeName($name);
echo $name."<br />";
function f1() {
    echo 'func1'.'<br />';
}
function f2() {
    echo 'func2'.'<br />';
}
$func = 'f1';
$func();
$func = 'f2';
$func();
?>