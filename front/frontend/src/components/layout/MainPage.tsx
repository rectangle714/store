import Container from '@mui/material/Container';
import ItemGrid from './ItemGrid';
import Styls from'../../styles/layout/MainPage.module.css'
import Slider from './Slider';
import GoogleLoginButton from '../auth/GoogleLoginButton';

const MainPage = () => {

    return (
        <Container maxWidth="md" fixed className={Styls.container}>
          <Slider/>
          <ItemGrid/>
        </Container>
    )
}

export default MainPage;