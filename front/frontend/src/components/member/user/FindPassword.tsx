import { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { Button, TextField, ButtonGroup, InputAdornment, Alert } from '@mui/material';
import { Container } from "@mui/joy";
import { Tabs, Tab, Box } from '@mui/material';
import EmailIcon from '@mui/icons-material/Email';
import KeyIcon from '@mui/icons-material/Key';

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
  }

function a11yProps(index: number) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

function TabPanel(props: TabPanelProps) {
    const { children, value, index, ...other } = props;
  
    return (
      <div
        role="tabpanel"
        hidden={value !== index}
        id={`simple-tabpanel-${index}`}
        aria-labelledby={`simple-tab-${index}`}
        {...other}
      >
        {value === index && (
          <Box sx={{ p: 3 }}>
            {children}
          </Box>
        )}
      </div>
    );
  }


const FindPassword = () => {
    const [value, setValue] = useState(0);
    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
      setValue(newValue);
    };

    return (
        <>
           <Container maxWidth="lg" fixed>
                <section style={{
                    minHeight:'84.8vh',
                    display:'flex',
                    flexDirection:'column',
                    alignItems:'center',
                    justifyContent:'center',
                    fontSize:'calc(10px + 2vmin)'
                }}>
                    <div style={{fontWeight:800, fontSize:'28px', lineHeight:'20px', textAlign:'center', marginBottom:'20px'}}>패스워드 찾기</div>
                        <Box sx={{ borderBottom: 1, borderColor: 'divider'}}>
                            <Tabs centered={true} value={value} onChange={handleChange} aria-label="basic tabs example">
                                <Tab label="이메일 인증" {...a11yProps(0)} />
                            </Tabs>
                        </Box>
                        {/* 이메일 인증 */}
                        <TabPanel value={value} index={0}>
                        <form>
                            <div>
                                <TextField 
                                    label='이름'
                                    variant='standard'
                                    autoComplete='true'
                                    style={{width:'290px', height: '60px'}}
                                    placeholder="이름을 입력해주세요"
                                    id='name'
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position='start'>
                                            <EmailIcon />
                                            </InputAdornment>
                                        ),
                                    }}
                                    />
                            </div>
                            <div style={{marginTop:'30px'}}>
                                <TextField 
                                    label='이메일'
                                    type='text'
                                    variant='standard'
                                    style={{width:'290px'}}
                                    placeholder="이메일을 입력해주세요"
                                    id='email'
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position='start'>
                                            <KeyIcon />
                                            </InputAdornment>
                                        ),
                                    }}
                                    />
                            </div>
                            <Button style={{width: '100%', height:'50px', marginTop:'30px'}} color='success' variant='contained'>확인</Button>
                        </form>
                        <div style={{
                                paddingTop: 20,
                            }}>
                        </div>
                        <div style={{
                                paddingTop: 10,
                                fontSize: 12, 
                                color: 'red',
                                textAlign: 'center',
                                paddingLeft: 30
                            }}>
                        </div>
                    </TabPanel>
                    <div style={{
                            paddingTop: 20,
                        }}>
                    </div>
                    <div style={{
                            paddingTop: 10,
                            fontSize: 12, 
                            color: 'red',
                            textAlign: 'center',
                            paddingLeft: 30
                        }}>
                    </div>
                </section>
            </Container>
        </>
    )
}

export default FindPassword;