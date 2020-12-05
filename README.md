# MyAndroidSample
This application shows recyclerview with following functionality:
1.Pull to refresh using swipe to refresh layout

2.Lazy loading using endless scrolling .
Note :
The api does not support any query parameter currently to fetch new set of items based on page limit when doing lazy loading
So currently fetching the same result always .Logic in place in case we pass a page limit when doing lazy loading

3.Added unit test cases for viewmodel and repository class

4.Added UI test cases using Espresso