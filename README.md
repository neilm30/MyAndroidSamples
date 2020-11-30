# MyAndroidSample
This application shows recyclerview with following functionality:
1.Pull to refresh using swipe to refresh layout
2.Lazy loading using endless scrolling .
Note :
The api does not support any query parameter currently to fetch new set of items based on page limit when doing lazy loading
So currently fetching the same result always .Logic in place in case we pass a page limit when doing lazy loading
3.Written unit test cases for viewmodel and repository class
4.Espresso test cases : To validate espresso tests added a delay  when validating recyclerview items .Ideally as per android docs need to use Espresso Idling but could not complete it
